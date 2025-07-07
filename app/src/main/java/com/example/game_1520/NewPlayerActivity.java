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

/**
 * Activity class for creating a new player in the game.
 * Allows the user to input player details such as name and gender.
 */
public class NewPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner genderSpinner; // Spinner for selecting the player's gender.

    /**
     * Called when the activity is created.
     * Sets up the layout, initializes UI components, and handles user interactions.
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        EdgeToEdge.enable(this);

        // Adjusts padding for system bars using window insets.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.new_player), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the gender spinner with options and sets its listener.
        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Male", "Female", "Other"}) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                return view;
            }
        });
        genderSpinner.setOnItemSelectedListener(this);

        // Sets up the "Save Player" button to save player details.
        findViewById(R.id.save_player_button).setOnClickListener(view -> {
            String playerName = ((EditText) findViewById(R.id.player_name_input)).getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();
            if (!playerName.isEmpty()) {
                Database db = new Database(this);
                db.insertPlayer(playerName, gender);
                finish();
            } else {
                // Displays an error message if the player name is empty.
                ((EditText) findViewById(R.id.player_name_input)).setError("Player name cannot be empty");
            }
        });
    }

    /**
     * Called when an item is selected in the gender spinner.
     * Updates the player icon based on the selected gender.
     * @param parent The AdapterView where the selection happened.
     * @param view The view within the AdapterView that was clicked.
     * @param position The position of the selected item in the adapter.
     * @param id The row ID of the selected item.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    /**
     * Called when no item is selected in the gender spinner.
     * No action is performed in this implementation.
     * @param parent The AdapterView where the selection happened.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed
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