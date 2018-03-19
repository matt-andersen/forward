package cryptoconverter.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    BigDecimal btcPrice;
    EditText btcInput;
    EditText dollarInput;
    RetrieveCryptoPrices retrieveCryptoPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Crypto Converter");
        try {
            retrieveCryptoPrices = new RetrieveCryptoPrices();
            retrieveCryptoPrices.execute().get();
            btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
            retrieveCryptoPrices.cancel(true);
            System.out.println("CURRENT BTC PRICE: " + btcPrice);
        } catch (Exception e) {
            System.out.println("MAD EXCEPTION: " + e);
        }

        dollarInput = findViewById(R.id.dollarInput);
        btcInput = findViewById(R.id.btcInput);

        btcInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!dollarInput.isFocused()) {

                    if (!btcInput.getText().toString().equals("")) {
                        BigDecimal costInDollars = btcPrice.multiply(new BigDecimal(btcInput.getText().toString()));

                        BigDecimal dollarNum = new BigDecimal(costInDollars.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

                        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

                        String newDollar = decimalFormat.format(dollarNum);

                        System.out.println("NEW DOLLAR NUM: " + dollarNum);

                        dollarInput.setText(newDollar);

                    } else {
                        dollarInput.setText("");
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

                if (!btcInput.isFocused()) {

                    System.out.println("IMPORTANT DOLLAR OUTPUT: " + dollarInput.getText().toString());

                    if (!dollarInput.getText().toString().replaceAll(",", "").equals("")) {

                        BigDecimal amountOfBtc = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(btcPrice, 8, BigDecimal.ROUND_HALF_EVEN);
                        btcInput.setText(amountOfBtc.toString());


                    } else {
                        btcInput.setText("");
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
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
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

            default:
                // unknown error
        }
        return super.onOptionsItemSelected(item);
    }

    void getCoinPrices() throws Exception {
//        RetrieveCryptoPrices retrieveCryptoPrices = new RetrieveCryptoPrices();
//        retrieveCryptoPrices.execute().get();
//        btcPrice = Integer.parseInt(retrieveCryptoPrices.getBtcPrice());
//        System.out.println("CURRENT BTC PRICE: " + btcPrice);
    }


}


