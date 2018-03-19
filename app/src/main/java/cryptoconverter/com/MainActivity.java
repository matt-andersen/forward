package cryptoconverter.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    BigDecimal btcPrice;
    BigDecimal ethPrice;

    EditText btcInput;
    EditText ethInput;

    EditText dollarInput;
    RetrieveCryptoPrices retrieveCryptoPrices;
    SharedPreferences preferences;
    public static String currency;
    ImageView currencyImg;
    TextView currencySymbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("value", MODE_PRIVATE);
        currency = preferences.getString("currency", "aud");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Crypto Converter");
        try {
            retrieveCryptoPrices = new RetrieveCryptoPrices();
            retrieveCryptoPrices.execute().get();
            btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
            ethPrice = new BigDecimal(retrieveCryptoPrices.getEthPrice());
            retrieveCryptoPrices.cancel(true);
            System.out.println("CURRENT BTC PRICE: " + btcPrice);
        } catch (Exception e) {
            System.out.println("EXCEPTION THROWN: " + e);
        }

        currencyImg = findViewById(R.id.currencyImg);
        currencySymbol = findViewById(R.id.currencySymbol);


        // Set currency flag icon
        switch (currency) {
            case ("aud"):
                currencyImg.setImageResource(R.drawable.aud);
                currencySymbol.setText("$");
                break;
            case ("usd"):
                currencyImg.setImageResource(R.drawable.usd);
                currencySymbol.setText("$");
                break;
            case ("gbp"):
                currencyImg.setImageResource(R.drawable.gbp);
                currencySymbol.setText("£");
                break;
        }

        dollarInput = findViewById(R.id.dollarInput);
        btcInput = findViewById(R.id.btcInput);
        ethInput = findViewById(R.id.ethInput);

        ethInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!dollarInput.isFocused() && !btcInput.isFocused()) {

                    if (!ethInput.getText().toString().equals("")) {
                        BigDecimal costInDollars = ethPrice.multiply(new BigDecimal(ethInput.getText().toString()));

                        BigDecimal dollarNum = new BigDecimal(costInDollars.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

                        String newDollar = decimalFormat.format(dollarNum);

                        System.out.println("NEW DOLLAR NUMMY: " + dollarNum);

                        dollarInput.setText(newDollar);

                        BigDecimal amountOfBtc = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(btcPrice, 8, BigDecimal.ROUND_HALF_EVEN);

                        btcInput.setText(amountOfBtc.toString());


                    } else {
                        dollarInput.setText("");
                        btcInput.setText("");
                    }
                }
            }

        });

        btcInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!dollarInput.isFocused() && !ethInput.isFocused()) {

                    if (!btcInput.getText().toString().equals("")) {
                        BigDecimal costInDollars = btcPrice.multiply(new BigDecimal(btcInput.getText().toString()));

                        BigDecimal dollarNum = new BigDecimal(costInDollars.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

                        String newDollar = decimalFormat.format(dollarNum);

                        System.out.println("NEW DOLLAR NUM: " + dollarNum);

                        dollarInput.setText(newDollar);

                        BigDecimal amountOfEth = new BigDecimal(newDollar.replaceAll(",", "")).divide(ethPrice, 8, BigDecimal.ROUND_HALF_EVEN);

                        ethInput.setText(amountOfEth.toString());

                    } else {
                        dollarInput.setText("");
                        ethInput.setText("");
                    }
                }
            }

        });

        dollarInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!btcInput.isFocused() && !ethInput.isFocused()) {

                    System.out.println("IMPORTANT DOLLAR OUTPUT: " + dollarInput.getText().toString());

                    if (!dollarInput.getText().toString().replaceAll(",", "").equals("")) {

                        BigDecimal amountOfBtc = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(btcPrice, 8, BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal amountOfEth = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(ethPrice, 8, BigDecimal.ROUND_HALF_EVEN);
                        btcInput.setText(amountOfBtc.toString());
                        ethInput.setText(amountOfEth.toString());


                    } else {
                        btcInput.setText("");
                        ethInput.setText("");
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.toolbar_refresh:
                try {
                    retrieveCryptoPrices.execute().get();
                    btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
                } catch (Exception ignored) {
                    System.out.println("DARDY: " + ignored);
                }
                System.out.println(btcPrice);
                try {
                    btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
                } catch (Exception ignored) {
                }
                System.out.println(btcPrice);
                Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}


