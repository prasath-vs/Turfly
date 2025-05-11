package com.example.turfly;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turfly.adapters.TurfAdapter;
import com.example.turfly.models.TurfModel;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class TurfBookingActivity extends AppCompatActivity {

    private RecyclerView turfRecyclerView;
    private List<TurfModel> turfList;
    private TurfAdapter turfAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_booking);

        // ✅ Initialize RecyclerView
        turfRecyclerView = findViewById(R.id.turfRecyclerView);
        turfRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // ✅ FIX: Attach Layout Manager

        turfList = new ArrayList<>();
        turfAdapter = new TurfAdapter(this, turfList, false); // Not admin mode
        turfRecyclerView.setAdapter(turfAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Turfs");
        loadTurfs();
    }

    private void loadTurfs() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                turfList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TurfModel turf = dataSnapshot.getValue(TurfModel.class);
                    if (turf != null) {
                        turfList.add(turf);
                    }
                }
                turfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
