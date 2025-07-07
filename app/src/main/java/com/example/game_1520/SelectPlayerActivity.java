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

/**
 * Activity class for selecting a player in the game.
 * Displays a list of players and allows the user to select one or add a new player.
 */
public class SelectPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner playerSpinner; // Spinner for displaying player names.
    private Map<Integer, List<String>> players; // Map of player IDs to their details.
    private int selectedPlayerId = -1; // ID of the selected player, default is -1 for no selection.

    /**
     * Called when the activity is created.
     * Sets up the layout, initializes UI components, and handles user interactions.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        EdgeToEdge.enable(this);

        // Adjusts padding for system bars using window insets.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.select_player), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetches all players from the database.
        Database db = new Database(this);
        players = db.getAllPlayers();

        // If no players exist, navigate to the NewPlayerActivity.
        if (players.isEmpty()) {
            startActivity(new Intent(this, NewPlayerActivity.class));
            finish();
        } else {
            // Extracts player names from the map and populates the spinner.
            List<String> playerNames = new ArrayList<>();
            for (List<String> playerInfo : players.values()) {
                if (!playerInfo.isEmpty()) {
                    playerNames.add(playerInfo.get(0)); // Assumes the first element is the name.
                }
            }
            playerSpinner = findViewById(R.id.player_spinner);
            playerSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerNames));
            playerSpinner.setSelection(-1, true); // Sets default selection to none.
            playerSpinner.setOnItemSelectedListener(this);
        }

        // Sets up the "Add Player" button to navigate to the NewPlayerActivity.
        findViewById(R.id.add_player_button).setOnClickListener(view -> {
            startActivity(new Intent(this, NewPlayerActivity.class));
            finish();
        });

        // Sets up the "Select Player" button to start the game with the selected player.
        findViewById(R.id.select_player_button).setOnClickListener(view -> {
            int selectedPosition = playerSpinner.getSelectedItemPosition();
            if (selectedPosition >= 0) {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("player_id", selectedPlayerId); // Passes the selected player ID.
                startActivity(intent);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    /**
     * Called when an item is selected in the player spinner.
     * Updates the selected player ID and player icon based on the selection.
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the selected item in the adapter.
     * @param id The row ID of the selected item.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Gets the player name at the selected position.
        String selectedPlayerName = parent.getItemAtPosition(position).toString();

        // Finds the player ID by matching the name in the players map.
        for (Map.Entry<Integer, List<String>> entry : players.entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().get(0).equals(selectedPlayerName)) {
                selectedPlayerId = entry.getKey();
                break;
            }
        }

        // Updates the player icon based on the selected player's gender.
        String gender = selectedPlayerId == -1 ? "" : players.get(selectedPlayerId).get(1);
        ImageView genderImage = findViewById(R.id.player_icon);
        if ("Male".equals(gender)) {
            genderImage.setImageResource(R.drawable.boy);
        } else if ("Female".equals(gender)) {
            genderImage.setImageResource(R.drawable.girl);
        } else {
            genderImage.setImageResource(R.drawable.user);
        }
    }

    /**
     * Called when no item is selected in the player spinner.
     * No action is performed in this implementation.
     * @param parent The AdapterView where the selection happened.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed.
    }

    /**
     * Handles the back button press event.
     * Overrides the default transition animation when navigating back.
     * @param view The view that triggered the event.
     */
    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}