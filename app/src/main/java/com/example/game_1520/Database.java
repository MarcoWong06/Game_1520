package com.example.game_1520;

import static android.content.Context.MODE_PRIVATE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database class for managing SQLite database operations related to players and game logs.
 */
public class Database {
    private final SQLiteDatabase db;

    /**
     * Constructor to initialize the database.
     * @param context The application context used to open or create the database.
     */
    public Database(Context context) {
        db = context.openOrCreateDatabase("game_1520.db", MODE_PRIVATE, null);
    }

    /**
     * Creates the necessary tables in the database if they do not already exist.
     * Tables:
     * - Player: Stores player information (id, name, gender).
     * - GamesLog: Stores game logs (id, player_id, gameDate, gameTime, winOrLose, round).
     */
    public void createTables() {
        db.execSQL("CREATE TABLE IF NOT EXISTS Player (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, gender TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS GamesLog (id INTEGER PRIMARY KEY AUTOINCREMENT, player_id INTEGER, gameDate TEXT, gameTime TEXT, winOrLose TEXT, round INTEGER, FOREIGN KEY(player_id) REFERENCES Player(id))");
    }

    /**
     * Inserts a new player into the Player table.
     * @param name The name of the player.
     * @param gender The gender of the player.
     */
    public void insertPlayer(String name, String gender) {
        db.execSQL("INSERT INTO Player (name, gender) VALUES (?, ?)", new Object[]{name, gender});
    }

    /**
     * Inserts a new game log into the GamesLog table.
     * @param playerId The ID of the player associated with the game log.
     * @param gameDate The date of the game.
     * @param gameTime The time of the game.
     * @param winOrLose The result of the game (win or lose).
     * @param round The round number of the game.
     */
    public void insertGameLog(int playerId, String gameDate, String gameTime, String winOrLose, int round) {
        db.execSQL("INSERT INTO GamesLog (player_id, gameDate, gameTime, winOrLose, round) VALUES (?, ?, ?, ?, ?)", new Object[]{playerId, gameDate, gameTime, winOrLose, round});
    }

    /**
     * Retrieves all players from the Player table.
     * @return A map where the key is the player ID and the value is a list containing the player's name and gender.
     */
    public Map<Integer, List<String>> getAllPlayers() {
        Map<Integer, List<String>> players = new HashMap<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Player", null);
        if (cursor.moveToFirst()) {
            do {
                int idCol = cursor.getColumnIndex("id");
                int nameCol = cursor.getColumnIndex("name");
                int genderCol = cursor.getColumnIndex("gender");
                if (idCol != -1 && nameCol != -1) {
                    int playerId = cursor.getInt(idCol);
                    String playerName = cursor.getString(nameCol);
                    String gender = cursor.getString(genderCol);
                    players.put(playerId, Arrays.asList(new String[]{playerName, gender}));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    /**
     * Retrieves all game logs from the GamesLog table, joined with player information.
     * @return A list of formatted strings representing game logs, including game date, time, player name, result, and round.
     */
    public List<String> getAllGameLogs() {
        List<String> gameLogs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT gl.gameDate, gl.gameTime, p.name, gl.winOrLose, gl.round FROM GamesLog gl JOIN Player p ON gl.player_id = p.id ORDER BY gl.id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int gameDateCol = cursor.getColumnIndex("gameDate");
                int gameTimeCol = cursor.getColumnIndex("gameTime");
                int nameCol = cursor.getColumnIndex("name");
                int winOrLoseCol = cursor.getColumnIndex("winOrLose");
                int roundCol = cursor.getColumnIndex("round");
                if (gameDateCol != -1 && gameTimeCol != -1 && nameCol != -1 && winOrLoseCol != -1 && roundCol != -1) {
                    String gameDate = cursor.getString(gameDateCol);
                    String gameTime = cursor.getString(gameTimeCol);
                    String playerName = cursor.getString(nameCol);
                    String winOrLose = cursor.getString(winOrLoseCol);
                    int round = cursor.getInt(roundCol);

                    String logEntry = String.format("%s, %s, %s, %s at round %d", gameDate, gameTime, playerName, winOrLose, round);
                    gameLogs.add(logEntry);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return gameLogs;
    }
}