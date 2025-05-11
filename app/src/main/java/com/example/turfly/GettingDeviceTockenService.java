package com.example.turfly;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class GettingDeviceTockenService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("DEVICE TOKEN", "New Token: " + token);
        // Send token to server if needed
    }

    public void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("DEVICE TOKEN", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get the new FCM token
                    String token = task.getResult();
                    Log.d("DEVICE TOKEN", "FCM Token: " + token);
                });
    }
}
