package cryptoconverter.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences preferences;
    String currentCurrency;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Get chosen currency from shared preferences
        preferences = getSharedPreferences("value", MODE_PRIVATE);
        currentCurrency = preferences.getString("currency", "aud");

        // Select the radio button which is already 'chosen'
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
            }
        });
    }

    // When tne physical/device button is pressed, save the currency to shared prefs
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveCurrency();
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
}



