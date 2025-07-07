package com.example.game_1520;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Activity class for managing the hand-guessing game interface and logic.
 */
public class GameActivity extends AppCompatActivity {
    private Game game; // Instance of the Game class to manage game logic.
    private Database db; // Instance of the Database class for storing game logs.
    private int playerId; // ID of the player participating in the game.

    /**
     * Called when the activity is created. Initializes the UI and game logic.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        EdgeToEdge.enable(this);

        // Adjusts padding to account for system bars.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database(this); // Initializes the database.
        playerId = getIntent().getIntExtra("player_id", -1); // Retrieves the player ID from the intent.
        initGame(); // Initializes the game.

        // Sets up click listeners for the player's left hand image.
        ImageView leftHandImage = findViewById(R.id.left_hand_image);
        leftHandImage.setOnClickListener(view -> {
            if (game.playerLeftHandChange() == Game.HAND_CLOSED)
                leftHandImage.setImageResource(R.drawable.zero);
            else
                leftHandImage.setImageResource(R.drawable.five);
        });

        // Sets up click listeners for the player's right hand image.
        ImageView rightHandImage = findViewById(R.id.right_hand_image);
        rightHandImage.setOnClickListener(view -> {
            if (game.playerRightHandChange() == Game.HAND_CLOSED)
                rightHandImage.setImageResource(R.drawable.zero);
            else
                rightHandImage.setImageResource(R.drawable.five);
        });

        // Sets up click listeners for number buttons.
        findViewById(R.id.btn0).setOnClickListener(view -> {
            startNewGame(0);
            disableAllButtons();
        });

        findViewById(R.id.btn5).setOnClickListener(view -> {
            startNewGame(5);
            disableAllButtons();
        });

        findViewById(R.id.btn10).setOnClickListener(view -> {
            startNewGame(10);
            disableAllButtons();
        });

        findViewById(R.id.btn15).setOnClickListener(view -> {
            startNewGame(15);
            disableAllButtons();
        });

        findViewById(R.id.btn20).setOnClickListener(view -> {
            startNewGame(20);
            disableAllButtons();
        });

        // Sets up click listener for the continue button.
        findViewById(R.id.continue_button).setOnClickListener(view -> {
            startNewGame(-1);
            enableNumberButton();
        });
    }

    /**
     * Starts a new game round with the specified guess.
     * @param guess The guess value for the current round.
     */
    private void startNewGame(int guess) {
        Activity activity = this;
        TextView guessTextView = findViewById(R.id.guess);
        TextView roundTextView = findViewById(R.id.round);

        guessTextView.setText(R.string.loading);
        game.setGuess(guess);
        game.startGame(isCorrect -> {
            // Updates the computer's hand images based on the game state.
            ImageView computerLeftHand = findViewById(R.id.computer_left_hand);
            if (game.getComputerLeftHand() == Game.HAND_CLOSED)
                computerLeftHand.setImageResource(R.drawable.zero);
            else
                computerLeftHand.setImageResource(R.drawable.five);

            ImageView computerRightHand = findViewById(R.id.computer_right_hand);
            if (game.getComputerRightHand() == Game.HAND_CLOSED)
                computerRightHand.setImageResource(R.drawable.zero);
            else
                computerRightHand.setImageResource(R.drawable.five);

            // Displays the result of the game round.
            String guessText = game.getCurrentGuesser() == Game.GamePlayer.PLAYER ? "Your guess: " : "Computer guess: ";
            String matchText = game.getCurrentGuesser() == Game.GamePlayer.PLAYER ? "WIN" : "LOSE";
            if (isCorrect) {
                runOnUiThread(() -> {
                    guessTextView.setText(guessText + game.getGuess());
                    new AlertDialog.Builder(activity)
                            .setTitle("Information")
                            .setMessage("YOU " + matchText + "\n" + guessText + game.getGuess())
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Logs the game result in the database.
                                db.insertGameLog(playerId,
                                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy")),
                                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        matchText,
                                        game.getCurrentRound());
                                initGame();
                            }).show();
                });
            } else {
                runOnUiThread(() -> {
                    guessTextView.setText(guessText + game.getGuess());
                    if (guess == -1) {
                        enableNumberButton();
                    } else {
                        enableContinueButton();
                    }
                });
            }

            // Updates the round number and switches the guesser.
            if (game.switchGuesser() == Game.GamePlayer.COMPUTER) {
                runOnUiThread(() -> {
                    roundTextView.setText(String.valueOf(game.getCurrentRound()));
                });
            } else {
                runOnUiThread(() -> {
                    roundTextView.setText(String.valueOf(game.getCurrentRound()));
                });
            }
        });
    }

    /**
     * Initializes the game state and UI elements.
     */
    private void initGame() {
        game = new Game();
        enableNumberButton();

        // Resets hand images to the default state.
        ImageView leftHandImage = findViewById(R.id.left_hand_image);
        leftHandImage.setImageResource(R.drawable.zero);
        ImageView rightHandImage = findViewById(R.id.right_hand_image);
        rightHandImage.setImageResource(R.drawable.zero);
        ImageView computerLeftHand = findViewById(R.id.computer_left_hand);
        computerLeftHand.setImageResource(R.drawable.zero);
        ImageView computerRightHand = findViewById(R.id.computer_right_hand);
        computerRightHand.setImageResource(R.drawable.zero);

        // Resets round and guess text views.
        TextView roundTextView = findViewById(R.id.round);
        roundTextView.setText(String.valueOf(game.getCurrentRound()));
        TextView guessTextView = findViewById(R.id.guess);
        guessTextView.setText(R.string.you_guess_first);
    }

    /**
     * Enables the continue button and disables number buttons.
     */
    private void enableContinueButton() {
        findViewById(R.id.btn0).setEnabled(false);
        findViewById(R.id.btn0).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn5).setEnabled(false);
        findViewById(R.id.btn5).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn10).setEnabled(false);
        findViewById(R.id.btn10).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn15).setEnabled(false);
        findViewById(R.id.btn15).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn20).setEnabled(false);
        findViewById(R.id.btn20).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.continue_button).setEnabled(true);
        findViewById(R.id.continue_button).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }

    /**
     * Enables number buttons and disables the continue button.
     */
    private void enableNumberButton() {
        findViewById(R.id.btn0).setEnabled(true);
        findViewById(R.id.btn0).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        findViewById(R.id.btn5).setEnabled(true);
        findViewById(R.id.btn5).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        findViewById(R.id.btn10).setEnabled(true);
        findViewById(R.id.btn10).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        findViewById(R.id.btn15).setEnabled(true);
        findViewById(R.id.btn15).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        findViewById(R.id.btn20).setEnabled(true);
        findViewById(R.id.btn20).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        findViewById(R.id.continue_button).setEnabled(false);
        findViewById(R.id.continue_button).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    /**
     * Disables all buttons in the UI.
     */
    private void disableAllButtons() {
        findViewById(R.id.btn0).setEnabled(false);
        findViewById(R.id.btn5).setEnabled(false);
        findViewById(R.id.btn10).setEnabled(false);
        findViewById(R.id.btn15).setEnabled(false);
        findViewById(R.id.btn20).setEnabled(false);
        findViewById(R.id.continue_button).setEnabled(false);
        findViewById(R.id.btn0).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn5).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn10).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn15).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.btn20).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.continue_button).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    /**
     * Handles the back button press event.
     * @param view The view that triggered the event.
     */
    public void onBackPressed(View view) {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}