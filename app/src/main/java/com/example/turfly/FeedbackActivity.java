package com.example.turfly;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    EditText etName, etEmail, etFeedback;
    Button btnSubmit;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etFeedback = findViewById(R.id.et_feedback);
        btnSubmit = findViewById(R.id.btn_submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feedback");

        btnSubmit.setOnClickListener(v -> {
            saveFeedback();
        });
    }

    private void saveFeedback() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String feedback = etFeedback.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(feedback)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        FeedbackModel feedbackData = new FeedbackModel(id, name, email, feedback);
        databaseReference.child(id).setValue(feedbackData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FeedbackActivity.this, "Feedback Submitted!", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etEmail.setText("");
                    etFeedback.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(FeedbackActivity.this, "Failed to Submit", Toast.LENGTH_SHORT).show());
    }
}
