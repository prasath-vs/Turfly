package com.example.turfly;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfly.adapters.TurfAdapter;
import com.example.turfly.models.TurfModel;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView turfListView;
    private Button btnAddTurf;
    private ArrayList<TurfModel> turfList;
    private TurfAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize Views
        turfListView = findViewById(R.id.turfListView);
        btnAddTurf = findViewById(R.id.btnAddTurf);
//        btnDeleteTurf = findViewById(R.id.btnDeleteTurf);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Turfs");

        // Initialize List and Adapter
        turfList = new ArrayList<>();
        adapter = new TurfAdapter(this, turfList, true);
        turfListView.setLayoutManager(new LinearLayoutManager(this));
        turfListView.setAdapter(adapter);

        // Load turfs with real-time updates
        loadTurfs();

        // Add Turf Button
        btnAddTurf.setOnClickListener(v -> startActivity(new Intent(this, AddTurfActivity.class)));

        // Delete Turf Button
//        btnDeleteTurf.setOnClickListener(v -> showDeleteDialog());
    }

    private void loadTurfs() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TurfModel turf = snapshot.getValue(TurfModel.class);
                if (turf != null) {
                    turfList.add(turf);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TurfModel updatedTurf = snapshot.getValue(TurfModel.class);
                if (updatedTurf != null) {
                    for (int i = 0; i < turfList.size(); i++) {
                        if (turfList.get(i).getId().equals(updatedTurf.getId())) {
                            turfList.set(i, updatedTurf);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                TurfModel deletedTurf = snapshot.getValue(TurfModel.class);
                if (deletedTurf != null) {
                    for (int i = 0; i < turfList.size(); i++) {
                        if (turfList.get(i).getId().equals(deletedTurf.getId())) {
                            turfList.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Not needed for this case
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load turfs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Turf");

        // Create input fields
        EditText inputTurfName = new EditText(this);
        inputTurfName.setHint("Enter Turf Name");

        EditText inputTurfLocation = new EditText(this);
        inputTurfLocation.setHint("Enter Turf Location");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(inputTurfName);
        layout.addView(inputTurfLocation);

        builder.setView(layout);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            String turfName = inputTurfName.getText().toString().trim();
            String turfLocation = inputTurfLocation.getText().toString().trim();
            if (!turfName.isEmpty() && !turfLocation.isEmpty()) {
                deleteTurf(turfName, turfLocation);
            } else {
                Toast.makeText(this, "Please enter both Turf Name and Location", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteTurf(String name, String location) {
        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot turfSnapshot : snapshot.getChildren()) {
                    TurfModel turf = turfSnapshot.getValue(TurfModel.class);
                    if (turf != null && turf.getLocation().equalsIgnoreCase(location)) {
                        turfSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> Toast.makeText(AdminDashboardActivity.this, "Turf deleted successfully!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(AdminDashboardActivity.this, "Failed to delete turf", Toast.LENGTH_SHORT).show());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(AdminDashboardActivity.this, "Turf not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
