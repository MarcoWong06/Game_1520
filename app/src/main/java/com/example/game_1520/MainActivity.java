package com.example.game_1520;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Main activity class for the game application.
 * Handles navigation to other activities and manages background music.
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer; // MediaPlayer instance for playing background music.

    /**
     * Called when the activity is created.
     * Sets up the layout, initializes the database, and starts background music.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjusts padding for system bars using window insets.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes and starts background music.
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Initializes the database and creates necessary tables.
        Database db = new Database(this);
        db.createTables();
    }

    /**
     * Starts the game by navigating to the SelectPlayerActivity.
     * @param view The view that triggered the event.
     */
    public void startGame(View view) {
        try {
            Intent intent = new Intent(this, SelectPlayerActivity.class);
            startActivity(intent);
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.d("MainActivity", "Error starting game: " + e.getMessage());
        }
    }

    /**
     * Navigates to the RecordsActivity to view game records.
     * @param view The view that triggered the event.
     */
    public void viewRecords(View view) {
        try {
            Intent intent = new Intent(this, RecordsActivity.class);
            startActivity(intent);
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.d("MainActivity", "Error viewing records: " + e.getMessage());
        }
    }

    /**
     * Closes the application.
     * @param view The view that triggered the event.
     */
    public void closeApp(View view) {
        finish();
        System.exit(0);
    }

    /**
     * Opens the HowToPlayActivity to display instructions on how to play the game.
     * @param view The view that triggered the event.
     */
    public void howToPlay(View view) {
        try {
            Intent intent = new Intent(this, HowToPlayActivity.class);
            startActivity(intent);
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.d("MainActivity", "Error opening how to play: " + e.getMessage());
        }
    }

    /**
     * Called when the activity is destroyed.
     * Releases the MediaPlayer resources to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}