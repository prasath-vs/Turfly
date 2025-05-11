package com.example.turfly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TurfBookingFormActivity extends AppCompatActivity {

    private EditText nameInput, ageInput, phoneInput, playersInput, hoursInput;
    private Button viewEstimateBtn;
    private String turfName, turfLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_booking_form);

        // Initialize UI components
        nameInput = findViewById(R.id.input_name);
        ageInput = findViewById(R.id.input_age);
        phoneInput = findViewById(R.id.input_phone);
        playersInput = findViewById(R.id.input_players);
        hoursInput = findViewById(R.id.input_hours);
        viewEstimateBtn = findViewById(R.id.btn_view_estimate);

        // Get data from intent
        Intent intent = getIntent();
        turfName = intent.getStringExtra("turf_name");
        turfLocation = intent.getStringExtra("turf_location");
        Log.d("TurfBookingForm", "Received Turf Name: " + turfName);
        Log.d("TurfBookingForm", "Received Turf Location: " + turfLocation);

        // Handle missing data
        if (turfName == null || turfName.isEmpty()) turfName = "Unknown Turf";
        if (turfLocation == null || turfLocation.isEmpty()) turfLocation = "Unknown Location";

        viewEstimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String age = ageInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String players = playersInput.getText().toString().trim();
                String hours = hoursInput.getText().toString().trim();

                if (name.isEmpty() || age.isEmpty() || phone.isEmpty() || players.isEmpty() || hours.isEmpty()) {
                    Toast.makeText(TurfBookingFormActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Move to Estimate Activity
                Intent estimateIntent = new Intent(TurfBookingFormActivity.this, TurfEstimateActivity.class);
                estimateIntent.putExtra("turf_name", turfName);
                estimateIntent.putExtra("turf_location", turfLocation);
                estimateIntent.putExtra("players", Integer.parseInt(players));
                estimateIntent.putExtra("hours", Integer.parseInt(hours));
                estimateIntent.putExtra("phone", phone);
                startActivity(estimateIntent);
            }
        });
    }
}
