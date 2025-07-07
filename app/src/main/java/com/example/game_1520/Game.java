package com.example.game_1520;

import org.json.JSONObject;

/**
 * Represents the game logic for a hand-guessing game.
 */
public class Game {
    /**
     * Enum representing the current player guessing in the game.
     */
    public enum GamePlayer {
        COMPUTER, PLAYER
    }

    // Constants representing the states of hands.
    public static final int HAND_OPEN = 5;
    public static final int HAND_CLOSED = 0;

    // Variables to store the state of the computer's and player's hands.
    private int computerLeftHand;
    private int computerRightHand;
    private int playerLeftHand;
    private int playerRightHand;

    // The current guess value and round number.
    private int guess;
    private int currentRound;

    // The current player guessing in the game.
    private GamePlayer currentGuesser = GamePlayer.PLAYER;

    /**
     * Constructor to initialize the game with default values.
     */
    public Game() {
        this.computerLeftHand = HAND_CLOSED;
        this.computerRightHand = HAND_CLOSED;
        this.playerLeftHand = HAND_CLOSED;
        this.playerRightHand = HAND_CLOSED;
        this.guess = 0;
        this.currentRound = 1;
    }

    /**
     * Sets the state of the computer's hands.
     * @param leftHand The state of the computer's left hand (open or closed).
     * @param rightHand The state of the computer's right hand (open or closed).
     */
    public void setComputerHand(int leftHand, int rightHand) {
        this.computerLeftHand = leftHand;
        this.computerRightHand = rightHand;
    }

    /**
     * Gets the state of the computer's left hand.
     * @return The state of the computer's left hand.
     */
    public int getComputerLeftHand() {
        return computerLeftHand;
    }

    /**
     * Gets the state of the computer's right hand.
     * @return The state of the computer's right hand.
     */
    public int getComputerRightHand() {
        return computerRightHand;
    }

    /**
     * Gets the current guess value.
     * @return The current guess value.
     */
    public int getGuess() {
        return guess;
    }

    /**
     * Gets the current round number.
     * @return The current round number.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Toggles the state of the player's left hand and returns the new state.
     * @return The new state of the player's left hand.
     */
    public int playerLeftHandChange() {
        if (playerLeftHand == HAND_OPEN) {
            playerLeftHand = HAND_CLOSED;
            return HAND_CLOSED;
        } else {
            playerLeftHand = HAND_OPEN;
            return HAND_OPEN;
        }
    }

    /**
     * Toggles the state of the player's right hand and returns the new state.
     * @return The new state of the player's right hand.
     */
    public int playerRightHandChange() {
        if (playerRightHand == HAND_OPEN) {
            playerRightHand = HAND_CLOSED;
            return HAND_CLOSED;
        } else {
            playerRightHand = HAND_OPEN;
            return HAND_OPEN;
        }
    }

    /**
     * Sets the current guess value.
     * @param guess The guess value to set.
     */
    public void setGuess(int guess) {
        this.guess = guess;
    }

    /**
     * Checks if the current guess is correct based on the total of all hands.
     * @return True if the guess is correct, false otherwise.
     */
    public boolean isGuessCorrect() {
        int total = computerLeftHand + computerRightHand + playerLeftHand + playerRightHand;
        return guess == total;
    }

    /**
     * Starts the game and processes the computer's guess asynchronously.
     * @param listener The listener to handle the result of the game.
     */
    public void startGame(OnGameResultListener listener) {
        GetComputerGuessThread thread = new GetComputerGuessThread(new OnComputerGuessListener() {
            @Override
            public void onGuessReceived(JSONObject json) {
                int left = json.optInt("left", HAND_CLOSED);
                int right = json.optInt("right", HAND_CLOSED);
                int guessValue = json.optInt("guess", 0);

                setComputerHand(left, right);
                if (currentGuesser == GamePlayer.COMPUTER) {
                    setGuess(guessValue);
                }

                boolean result = isGuessCorrect();
                if (listener != null) {
                    listener.onResult(result);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onResult(false);
                }
            }
        });
        currentRound++;
        thread.start();
    }

    /**
     * Interface for handling the result of the game.
     */
    public interface OnGameResultListener {
        /**
         * Called when the result of the game is available.
         * @param isCorrect True if the guess was correct, false otherwise.
         */
        void onResult(boolean isCorrect);
    }

    /**
     * Gets the current player guessing in the game.
     * @return The current player guessing.
     */
    public GamePlayer getCurrentGuesser() {
        return currentGuesser;
    }

    /**
     * Switches the current guesser to the other player.
     * @return The new current guesser.
     */
    public GamePlayer switchGuesser() {
        if (currentGuesser == GamePlayer.PLAYER) {
            currentGuesser = GamePlayer.COMPUTER;
        } else {
            currentGuesser = GamePlayer.PLAYER;
        }
        return GamePlayer.PLAYER;
    }
}