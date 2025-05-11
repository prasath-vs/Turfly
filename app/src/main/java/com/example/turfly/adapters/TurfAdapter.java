package com.example.turfly.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.turfly.R;
import com.example.turfly.TurfBookingFormActivity;
import com.example.turfly.models.TurfModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TurfAdapter extends RecyclerView.Adapter<TurfAdapter.ViewHolder> {

    private Context context;
    private List<TurfModel> turfList;
    private boolean adminMode; // ✅ Admin mode check

    public TurfAdapter(Context context, List<TurfModel> turfList, boolean adminMode) {
        this.context = context;
        this.turfList = turfList;
        this.adminMode = adminMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.turf_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TurfModel turf = turfList.get(position);

        // ✅ Setting Turf Details
        holder.turfName.setText(turf.getName());
        holder.turfRating.setText("⭐ " + turf.getRating());
        holder.turfArea.setText(turf.getArea());

        // ✅ Load Turf Image from Cloudinary
        Glide.with(context).load(turf.getImageUrl()).into(holder.turfImage);

        // ✅ Open Location in Google Maps
        holder.turfLocation.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(turf.getMapsLink())); // 🔹 FIXED getLocationUrl()
            context.startActivity(intent);
        });

        // ✅ Booking Functionality
        holder.bookNowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, TurfBookingFormActivity.class);
            intent.putExtra("turf_name", turf.getName());
            intent.putExtra("turf_location", turf.getMapsLink()); // 🔹 FIXED getLocationUrl()
            context.startActivity(intent);
        });

        // ✅ Admin-only Delete Functionality
        if (adminMode) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Turf")
                        .setMessage("Are you sure you want to delete this turf?")
                        .setPositiveButton("Yes", (dialog, which) -> deleteTurf(turf.getId()))
                        .setNegativeButton("No", null)
                        .show();
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return turfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView turfName, turfRating, turfArea;
        ImageView turfImage;
        Button bookNowBtn, deleteButton, turfLocation; // ✅ Ensure IDs match XML

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            turfName = itemView.findViewById(R.id.turfName);
            turfRating = itemView.findViewById(R.id.turfRating);
            turfArea = itemView.findViewById(R.id.turfArea);
            turfImage = itemView.findViewById(R.id.turfImage);
            bookNowBtn = itemView.findViewById(R.id.bookNowBtn);
            turfLocation = itemView.findViewById(R.id.turfLocation);
            deleteButton = itemView.findViewById(R.id.btn_delete); // ✅ Ensure this exists in XML
        }
    }

    private void deleteTurf(String turfId) {
        DatabaseReference turfRef = FirebaseDatabase.getInstance().getReference("Turfs").child(turfId);
        turfRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Turf deleted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show());
    }
}
