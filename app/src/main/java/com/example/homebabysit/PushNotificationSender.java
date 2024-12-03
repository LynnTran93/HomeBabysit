package com.example.homebabysit;


import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushNotificationSender {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AIzaSyDphHA1Z0jpoXIT5NJ6FBi6K4AsTrep8sM"; // Replace with your Firebase server key

    public static void sendNotification(String deviceToken, String title, String message) {
        // Start a new thread for network operations
        new Thread(() -> {
            try {
                // Prepare the request payload
                JSONObject payload = new JSONObject();
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);
                payload.put("to", deviceToken);
                payload.put("notification", notification);

                // Open connection to FCM
                URL url = new URL(FCM_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                // Write the payload
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                // Get response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("FCM", "Notification sent successfully!");
                } else {
                    Log.d("FCM", "Failed to send notification. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("FCM", "Error Sending Notification", e);
            }
        }).start();
    }
}
