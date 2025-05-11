package com.example.turfly;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;

public class TurfEstimateActivity extends AppCompatActivity implements PaymentResultListener {

    TextView txtTurfName, txtLocation, txtPlayers, txtHours, txtTotalAmount;
    Button btnPayNow;

    String turfName, location, phoneNumber;
    int players, hours;
    double pricePerHour = 100.0;
    double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_estimate);

        txtTurfName = findViewById(R.id.txtTurfName);
        txtLocation = findViewById(R.id.txtLocation);
        txtPlayers = findViewById(R.id.txtPlayers);
        txtHours = findViewById(R.id.txtHours);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        btnPayNow = findViewById(R.id.btnPayNow);

        // Get data from Intent
        Intent intent = getIntent();
        turfName = intent.getStringExtra("turf_name");
        location = intent.getStringExtra("turf_location");
        Log.d("TurfEstimate", "Received Turf Name: " + turfName);
        Log.d("TurfEstimate", "Received Location: " + location);
        players = intent.getIntExtra("players", 1);
        hours = intent.getIntExtra("hours", 1);
        phoneNumber = intent.getStringExtra("phone");

        // Handle null values
//        if (turfName == null || turfName.isEmpty()) turfName = "Unknown Turf";
        if (turfName == null || turfName.isEmpty()) {
            turfName = "Unknown Turf";
            Log.e("TurfEstimate", "Turf name is NULL");
        }
        if (location == null || location.isEmpty()) {
            location = "Unknown Location";
            Log.e("TurfEstimate", "Turf location is NULL");
        }
//        if (location == null || location.isEmpty()) location = "Unknown Location";
        if (phoneNumber == null || phoneNumber.isEmpty()) phoneNumber = "N/A";

        // Calculate total amount
        totalAmount = hours * pricePerHour;
        txtTurfName.setText("Turf: " + turfName);
        txtLocation.setText("Location: " + location);
        txtPlayers.setText("Players: " + players);
        txtHours.setText("Hours: " + hours);
        txtTotalAmount.setText("Total Amount: ₹" + totalAmount);

        // Initialize Razorpay
        Checkout.preload(this);

        // Pay Now Button Click
        btnPayNow.setOnClickListener(v -> startPayment());
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_9sVXb9hJHWB7yj"); // Replace with your Razorpay API Key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Turfly Turf Booking");
            options.put("description", "Payment for " + turfName);
            options.put("currency", "INR");
            options.put("amount", totalAmount * 100); // Amount in paisa
            options.put("prefill.contact", phoneNumber);
            options.put("otp_reading", false); // Disabling OTP reading to fix IntentReceiver leak

            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Payment Initialization Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful! ID: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        saveBookingToFirebase();
    }

    @Override
    public void onPaymentError(int code, String response) {
        String errorMessage = "Payment Failed!";
        if (code == Checkout.NETWORK_ERROR) errorMessage = "Network error! Check your connection.";
        else if (code == Checkout.INVALID_OPTIONS) errorMessage = "Invalid payment options.";
        else if (code == Checkout.PAYMENT_CANCELED) errorMessage = "Payment was cancelled.";
        else if (code == Checkout.TLS_ERROR) errorMessage = "Security error! Update your app.";

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void saveBookingToFirebase() {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        String bookingId = bookingsRef.push().getKey();

        if (bookingId == null) {
            Toast.makeText(this, "Error! Booking ID generation failed.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> bookingData = new HashMap<>();
        bookingData.put("bookingId", bookingId);
        bookingData.put("turfName", turfName);
        bookingData.put("location", location);
        bookingData.put("players", players);
        bookingData.put("hours", hours);
        bookingData.put("totalAmount", totalAmount);
        bookingData.put("paymentStatus", "Paid");

        bookingsRef.child(bookingId).setValue(bookingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                    sendConfirmationSMS();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to store booking. Try again.", Toast.LENGTH_SHORT).show());
    }

    private void sendConfirmationSMS() {
        if (phoneNumber.equals("N/A")) {
            Toast.makeText(this, "No valid phone number provided!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            return;
        }

        String message = "Booking Confirmed!\nTurf: " + turfName + "\nLocation: " + location +
                "\nThank you for booking with Turfly!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Confirmation SMS Sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Checkout.clearUserData(this); // Clears any user session data from Razorpay
    }
}
