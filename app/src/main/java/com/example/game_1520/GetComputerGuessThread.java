package com.example.game_1520;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetComputerGuessThread extends Thread {
    public static final String url = "https://assign-mobileasignment-ihudikcgpf.cn-hongkong.fcapp.run";
    private final OnComputerGuessListener listener;

    public GetComputerGuessThread(OnComputerGuessListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();
            JSONObject json = new JSONObject(content.toString());
            if (listener != null) {
                listener.onGuessReceived(json);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }
}