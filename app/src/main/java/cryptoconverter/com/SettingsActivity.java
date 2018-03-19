package cryptoconverter.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences = getSharedPreferences("value", MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(SettingsActivity.this, MainActivity.class);

                RadioGroup radioButtonGroup = findViewById(R.id.currencyGroup);

                int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();

                String currency = "";

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
                }

                preferences.edit().putString("currency", currency).apply();

                startActivity(mainActivity);
            }
        });

    }


}
