package com.example.game_1520;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity class for displaying the "How to Play" screen of the game.
 */
public class HowToPlayActivity extends AppCompatActivity {
    /**
     * Called when the activity is created.
     * Sets up the layout and adjusts padding to account for system bars.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        EdgeToEdge.enable(this);

        // Adjusts padding for system bars using window insets.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.how_to_play), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Handles the back button press event.
     * Overrides the default transition animation when navigating back.
     * @param view The view that triggered the event.
     */
    public void onBackPressed(View view) {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}