package com.example.homebabysit;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushNotificationSender {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "YOUR_SERVER_KEY_HERE";

    public static void sendNotification(String deviceToken, String title, String message) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);
                payload.put("to", deviceToken);
                payload.put("notification", notification);

                URL url = new URL(FCM_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("PushNotificationSender", "Notification sent successfully");
                } else {
                    Log.e("PushNotificationSender", "Failed to send notification: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("PushNotificationSender", "Error sending notification", e);
            }
        }).start();
    }
}
