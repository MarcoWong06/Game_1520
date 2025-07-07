package com.example.game_1520;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

/**
 * Activity class for displaying game records.
 * Fetches and displays a list of game logs from the database.
 */
public class RecordsActivity extends AppCompatActivity {
    /**
     * Called when the activity is created.
     * Sets up the layout, adjusts padding for system bars, and initializes the records view.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        EdgeToEdge.enable(this);

        // Adjusts padding for system bars using window insets.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.records), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the records view by fetching game logs from the database.
        Database db = new Database(this);
        List<String> allGameLogs = db.getAllGameLogs();
        if (allGameLogs.isEmpty()) {
            // Updates the title to indicate no records are found.
            TextView recordsTitle = findViewById(R.id.records_title);
            recordsTitle.setText(R.string.no_records_found);
        } else {
            // Populates the ListView with game logs.
            ListView recordsListView = findViewById(R.id.records_list);
            recordsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allGameLogs));
        }
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