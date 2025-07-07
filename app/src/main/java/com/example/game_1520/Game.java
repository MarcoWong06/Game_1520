package com.example.game_1520;

import org.json.JSONObject;

public class Game {
    public enum GamePlayer {
        COMPUTER, PLAYER
    }
    public static final int HAND_OPEN = 5;
    public static final int HAND_CLOSED = 0;
    private int computerLeftHand;
    private int computerRightHand;
    private int playerLeftHand;
    private int playerRightHand;
    private int guess;
    private int currentRound;
    private GamePlayer currentGuesser = GamePlayer.PLAYER;

    public Game() {
        this.computerLeftHand = HAND_CLOSED;
        this.computerRightHand = HAND_CLOSED;
        this.playerLeftHand = HAND_CLOSED;
        this.playerRightHand = HAND_CLOSED;
        this.guess = 0;
        this.currentRound = 1;
    }

    public void setComputerHand(int leftHand, int rightHand) {
        this.computerLeftHand = leftHand;
        this.computerRightHand = rightHand;
    }

    public int getComputerLeftHand() {
        return computerLeftHand;
    }

    public int getComputerRightHand() {
        return computerRightHand;
    }

    public int getGuess() {
        return guess;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int playerLeftHandChange() {
        if (playerLeftHand == HAND_OPEN) {
            playerLeftHand = HAND_CLOSED;
            return HAND_CLOSED;
        } else {
            playerLeftHand = HAND_OPEN;
            return HAND_OPEN;
        }
    }

    public int playerRightHandChange() {
        if (playerRightHand == HAND_OPEN) {
            playerRightHand = HAND_CLOSED;
            return HAND_CLOSED;
        } else {
            playerRightHand = HAND_OPEN;
            return HAND_OPEN;
        }
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public boolean isGuessCorrect() {
        int total = computerLeftHand + computerRightHand + playerLeftHand + playerRightHand;
        return guess == total;
    }

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

    public interface OnGameResultListener {
        void onResult(boolean isCorrect);
    }

    public GamePlayer getCurrentGuesser() {
        return currentGuesser;
    }

    public GamePlayer switchGuesser() {
        if (currentGuesser == GamePlayer.PLAYER) {
            currentGuesser = GamePlayer.COMPUTER;
        } else {
            currentGuesser = GamePlayer.PLAYER;
        }
        return GamePlayer.PLAYER;
    }
}
