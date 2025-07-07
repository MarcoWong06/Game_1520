package com.example.game_1520;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner playerSpinner;
    Map<Integer, List<String>> players;
    int selectedPlayerId = -1; // Default value for no selection
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.select_player), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Database db = new Database(this);
        players = db.getAllPlayers();
        if (players.isEmpty()) {
            startActivity(new Intent(this, NewPlayerActivity.class));
            finish();
        } else {
            // Extract player names from the map
            List<String> playerNames = new ArrayList<>();
            for (List<String> playerInfo : players.values()) {
                if (!playerInfo.isEmpty()) {
                    playerNames.add(playerInfo.get(0)); // Assuming the first element is the name
                }
            }
            playerSpinner = findViewById(R.id.player_spinner);
            playerSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerNames));
            playerSpinner.setSelection(-1, true);
            playerSpinner.setOnItemSelectedListener(this);
        }

        findViewById(R.id.add_player_button).setOnClickListener(view -> {
            startActivity(new Intent(this, NewPlayerActivity.class));
            finish();
        });

        findViewById(R.id.select_player_button).setOnClickListener(view -> {
            int selectedPosition = playerSpinner.getSelectedItemPosition();
            if (selectedPosition >= 0) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("player_id", selectedPlayerId); // Pass the selected player ID
                startActivity(intent);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Get the player name at the selected position
        String selectedPlayerName = parent.getItemAtPosition(position).toString();
        // Find the player ID by matching the name in the players map
        for (Map.Entry<Integer, List<String>> entry : players.entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().get(0).equals(selectedPlayerName)) {
                selectedPlayerId = entry.getKey();
                break;
            }
        }
        // Update the player icon based on the selected player
        String gender;
        if (selectedPlayerId == -1) {
            gender = "";
        } else {
            gender = players.get(selectedPlayerId).get(1);
        }
        ImageView genderImage = findViewById(R.id.player_icon);
        if (gender.equals("Male")) {
            genderImage.setImageResource(R.drawable.boy);
        } else if (gender.equals("Female")) {
            genderImage.setImageResource(R.drawable.girl);
        } else {
            genderImage.setImageResource(R.drawable.user);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}
