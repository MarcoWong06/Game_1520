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

public class RecordsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.records), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the records view
        Database db = new Database(this);
        List<String> allGameLogs = db.getAllGameLogs();
        if (allGameLogs.isEmpty()) {
            TextView recordsTitle = findViewById(R.id.records_title);
            recordsTitle.setText(R.string.no_records_found);
        } else {
            ListView recordsListView = findViewById(R.id.records_list);
            recordsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allGameLogs));
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
