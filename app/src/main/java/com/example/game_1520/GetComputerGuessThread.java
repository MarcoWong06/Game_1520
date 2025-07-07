package com.example.game_1520;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Thread class for fetching the computer's guess asynchronously from a remote server.
 */
public class GetComputerGuessThread extends Thread {
    // URL of the remote server providing the computer's guess.
    public static final String url = "https://assign-mobileasignment-ihudikcgpf.cn-hongkong.fcapp.run";

    // Listener interface to handle the result or error of the fetch operation.
    private final OnComputerGuessListener listener;

    /**
     * Constructor to initialize the thread with a listener.
     * @param listener The listener to handle the computer's guess or errors.
     */
    public GetComputerGuessThread(OnComputerGuessListener listener) {
        this.listener = listener;
    }

    /**
     * Executes the thread's logic to fetch the computer's guess from the server.
     * Sends a GET request to the specified URL and parses the JSON response.
     * Calls the listener's methods to handle the result or errors.
     */
    @Override
    public void run() {
        try {
            // Creates a URL object and opens a connection.
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET"); // Sets the request method to GET.
            conn.setRequestProperty("Accept", "application/json"); // Specifies the expected response format.

            // Reads the response from the server.
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Closes the input stream and disconnects the connection.
            in.close();
            conn.disconnect();

            // Parses the response into a JSON object and passes it to the listener.
            JSONObject json = new JSONObject(content.toString());
            if (listener != null) {
                listener.onGuessReceived(json);
            }
        } catch (Exception e) {
            // Handles errors by passing them to the listener.
            if (listener != null) {
                listener.onError(e);
            }
        }
    }
}