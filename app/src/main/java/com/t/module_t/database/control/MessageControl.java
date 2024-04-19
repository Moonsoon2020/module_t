package com.t.module_t.database.control;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.t.module_t.database.config.ConfigLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageControl {

    public static void sendMessage(String title, String text, String id, Context context) {
        new SendMessageTask(title, text, id, context).execute();
    }

    private static class SendMessageTask extends AsyncTask<Void, Void, Integer> {
        private final String title;
        private final String text;
        private final String id;
        private final Context context;

        public SendMessageTask(String title, String text, String id, Context context) {
            this.title = title;
            this.text = text;
            this.id = id;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String FCM_AUTHORIZATION_KEY;
            try {
                FCM_AUTHORIZATION_KEY = ConfigLoader.getFCMAuthorizationKey(context);
            } catch (IOException e) {
                Log.w("Mess", e.toString());
                return null;
            }

            String url = "https://fcm.googleapis.com/fcm/send";

            // Создание тела уведомления
            JSONObject notificationBody = new JSONObject();
            JSONObject notification = new JSONObject();
            try {
                notification.put("title", title);
                notification.put("body", text);
//                notification.put("rr", "flfl");
                notificationBody.put("notification", notification);
                notificationBody.put("to", id);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                // Создание объекта URL и открытие соединения
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Установка метода запроса и заголовков
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", "key=" + FCM_AUTHORIZATION_KEY);

                // Включение вывода для отправки тела запроса
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = notificationBody.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Получение ответа
                return con.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            super.onPostExecute(responseCode);
            if (responseCode != null) {
                Log.d("send", String.valueOf(responseCode));
            }
        }
    }
}
