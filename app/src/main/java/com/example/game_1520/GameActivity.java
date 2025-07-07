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

public class GameActivity extends AppCompatActivity {
    private Game game;
    private Database db;
    private int playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new Database(this);
        playerId = getIntent().getIntExtra("player_id", -1);
        initGame();

        ImageView leftHandImage = findViewById(R.id.left_hand_image);
        leftHandImage.setOnClickListener(view -> {
            if (game.playerLeftHandChange() == Game.HAND_CLOSED)
                leftHandImage.setImageResource(R.drawable.zero);
            else
                leftHandImage.setImageResource(R.drawable.five);
        });

        ImageView rightHandImage = findViewById(R.id.right_hand_image);
        rightHandImage.setOnClickListener(view -> {
            if (game.playerRightHandChange() == Game.HAND_CLOSED)
                rightHandImage.setImageResource(R.drawable.zero);
            else
                rightHandImage.setImageResource(R.drawable.five);
        });

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

        findViewById(R.id.continue_button).setOnClickListener(view -> {
            startNewGame(-1);
            enableNumberButton();
        });
    }

    private void startNewGame(int guess) {
        Activity activity = this;
        TextView guessTextView = findViewById(R.id.guess);
        TextView roundTextView = findViewById(R.id.round);

        guessTextView.setText(R.string.loading);
        game.setGuess(guess);
        game.startGame(isCorrect -> {
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
            String guessText = game.getCurrentGuesser() == Game.GamePlayer.PLAYER ? "Your guess: " : "Computer guess: ";
            String matchText = game.getCurrentGuesser() == Game.GamePlayer.PLAYER ? "WIN" : "LOSE";
            if (isCorrect) {
                runOnUiThread(() -> {
                    guessTextView.setText(guessText + game.getGuess());
                    new AlertDialog.Builder(activity)
                            .setTitle("Information")
                            .setMessage("YOU " + matchText + "\n" + guessText + game.getGuess())
                            .setPositiveButton("OK", (dialog, which) -> {
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
            ;
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

    private void initGame() {
        game = new Game();
        enableNumberButton();
        ImageView leftHandImage = findViewById(R.id.left_hand_image);
        leftHandImage.setImageResource(R.drawable.zero);
        ImageView rightHandImage = findViewById(R.id.right_hand_image);
        rightHandImage.setImageResource(R.drawable.zero);
        ImageView computerLeftHand = findViewById(R.id.computer_left_hand);
        computerLeftHand.setImageResource(R.drawable.zero);
        ImageView computerRightHand = findViewById(R.id.computer_right_hand);
        computerRightHand.setImageResource(R.drawable.zero);

        TextView roundTextView = findViewById(R.id.round);
        roundTextView.setText(String.valueOf(game.getCurrentRound()));
        TextView guessTextView = findViewById(R.id.guess);
        guessTextView.setText(R.string.you_guess_first);
    }

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

    public void onBackPressed(View view) {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
