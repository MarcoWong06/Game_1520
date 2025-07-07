package com.example.game_1520;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_player), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize genderSpinner
        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Male", "Female", "Other"}) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                return view;
            }
        });

        // Set listener for genderSpinner
        genderSpinner.setOnItemSelectedListener(this);

        // OnClickListener for the "Save Player" button
        findViewById(R.id.save_player_button).setOnClickListener(view -> {
            // Logic to save the new player
            String playerName = ((EditText) findViewById(R.id.player_name_input)).getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();
            if (!playerName.isEmpty()) {
                Database db = new Database(this);
                db.insertPlayer(playerName, gender);
                finish();
            } else {
                // Show an error message if the player name is empty
                ((EditText) findViewById(R.id.player_name_input)).setError("Player name cannot be empty");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Change image src based on gender selection
        ImageView genderImage = findViewById(R.id.player_icon);
        String gender = parent.getItemAtPosition(position).toString();
        if (gender.equals("Male")) {
            genderImage.setImageResource(R.drawable.boy);
        } else if (gender.equals("Female")) {
            genderImage.setImageResource(R.drawable.girl);
        } else {
            genderImage.setImageResource(R.drawable.user);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}