package com.example.game_1520;

import org.json.JSONObject;

public interface OnComputerGuessListener {
    void onGuessReceived(JSONObject json);
    void onError(Exception e);
}