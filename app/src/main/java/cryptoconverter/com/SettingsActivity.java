package cryptoconverter.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    Toolbar toolbar;
    SharedPreferences preferences;
    String currentCurrency;
    String currentTheme;

    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        detector = new GestureDetectorCompat(this, this);

        // Set theme colour
        LinearLayout myLayout = findViewById(R.id.settingsActivity);
        myLayout.setBackgroundColor(Color.parseColor(MainActivity.themeColour));

        // Get chosen currency and theme from shared preferences
        preferences = getSharedPreferences("value", MODE_PRIVATE);
        currentCurrency = preferences.getString("currency", "aud");
        currentTheme = preferences.getString("theme", "aud");

        // Select the currency radio button which is already 'chosen'
        RadioButton currentRadio;
        switch (currentCurrency) {
            case ("aud"):
                currentRadio = findViewById(R.id.audButton);
                currentRadio.setChecked(true);
                break;
            case ("usd"):
                currentRadio = findViewById(R.id.usdButton);
                currentRadio.setChecked(true);
                break;
            case ("gbp"):
                currentRadio = findViewById(R.id.gbpButton);
                currentRadio.setChecked(true);
                break;
        }

        // Select the currency radio button which is already 'chosen'
        RadioButton currentThemeRadio;
        switch (currentTheme) {
            case ("light"):
                currentThemeRadio = findViewById(R.id.lightButton);
                currentThemeRadio.setChecked(true);
                break;
            case ("dark"):
                currentThemeRadio = findViewById(R.id.darkButton);
                currentThemeRadio.setChecked(true);
                break;
        }


        // Create the toolbar and add the back button icon
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // When tne back button icon is pressed, save the currency to shared prefs
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrency();
                saveTheme();
            }
        });
    }

    // When tne physical/device button is pressed, save the currency to shared prefs
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveCurrency();
        saveTheme();
        Intent mainActivity = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }


    public void saveCurrency() {
        Intent mainActivity = new Intent(SettingsActivity.this, MainActivity.class);

        // Find which radio button is selected and set the currency to be saved to shared prefs
        RadioGroup radioButtonGroup = findViewById(R.id.currencyGroup);
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        String currency;
        switch (radioButtonID) {
            case (R.id.audButton):
                currency = "aud";
                break;
            case (R.id.usdButton):
                currency = "usd";
                break;
            case (R.id.gbpButton):
                currency = "gbp";
                break;
            default:
                currency = "aud";
        }

        preferences.edit().putString("currency", currency).apply();
        startActivity(mainActivity);
    }

    public void saveTheme() {
        // Find which radio button is selected and set the theme to be saved to shared prefs
        RadioGroup radioButtonGroup = findViewById(R.id.themeGroup);
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        String theme;
        switch (radioButtonID) {
            case (R.id.lightButton):
                theme = "light";
                break;
            case (R.id.darkButton):
                theme = "dark";
                break;
            default:
                theme = "light";
                break;
        }

        preferences.edit().putString("theme", theme).apply();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        if (motionEvent.getRawX() < motionEvent1.getRawX()) {
            saveCurrency();
            saveTheme();
            Intent settingsActivity = new Intent(this, MainActivity.class);
            startActivity(settingsActivity);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}



