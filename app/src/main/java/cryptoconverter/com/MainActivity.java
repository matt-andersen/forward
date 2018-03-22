package cryptoconverter.com;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    Toolbar toolbar;

    BigDecimal btcPrice;
    BigDecimal ethPrice;

    EditText btcInput;
    EditText ethInput;
    EditText dollarInput;
    ImageView currencyImg;
    TextView currencySymbol;

    RetrieveCryptoPrices retrieveCryptoPrices;

    SharedPreferences preferences;

    // Used for RetrieveCryptoPrices to get the user's currency
    public static String currency;

    // Used to get the user's theme
    public static String themeColour;

    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detector = new GestureDetectorCompat(this, this);

        // Load preferences and get user's currency
        preferences = getSharedPreferences("value", MODE_PRIVATE);
        currency = preferences.getString("currency", "aud");

        // Get user's theme
        String theme = preferences.getString("theme", "light");

        // Set theme colour
        LinearLayout myLayout = findViewById(R.id.mainActivity);
        try {
            switch (theme) {
                case ("light"):
                    themeColour = "#f5f5f6";
                    break;
                case ("dark"):
                    themeColour = "#3c3c3c";
                    break;
                default:
                    themeColour = "#f5f5f6";
                    break;
            }
            myLayout.setBackgroundColor(Color.parseColor(preferences.getString("backgroundColour", themeColour)));
        } catch (Exception e) {
            myLayout.setBackgroundColor(Color.parseColor(preferences.getString("backgroundColour", "#f5f5f6")));
        }

        // Create toolbar and set title
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Forward - Crypto Converter");
        setSupportActionBar(toolbar);

        try {
            // Run retrieve crypto prices method and set BTC and ETH price
            retrieveCryptoPrices = new RetrieveCryptoPrices();
            retrieveCryptoPrices.execute().get();
            btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
            ethPrice = new BigDecimal(retrieveCryptoPrices.getEthPrice());
            retrieveCryptoPrices.cancel(true);
        } catch (Exception ignored) {
            System.out.println("RETRIEVE PRICES EXCEPTION: " + ignored);
        }

        setCurrencyImg();

        // Set input fields
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

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

                if (!dollarInput.isFocused() && !btcInput.isFocused()) {

                    changeEthPrice();

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

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

                if (!dollarInput.isFocused() && !ethInput.isFocused()) {

                    changeBtcPrice();
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

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {

                if (!btcInput.isFocused() && !ethInput.isFocused()) {
                    changeDollarPrice();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Used to create the dropdown menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_settings:
                // Go to settings activity
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.toolbar_refresh:
                try {
                    // Re-fetch crypto prices
                    retrieveCryptoPrices = new RetrieveCryptoPrices();
                    retrieveCryptoPrices.execute().get();
                    BigDecimal oldBtcPrice = btcPrice;
                    BigDecimal oldEthPrice = ethPrice;
                    btcPrice = new BigDecimal(retrieveCryptoPrices.getBtcPrice());
                    ethPrice = new BigDecimal(retrieveCryptoPrices.getEthPrice());

                    // Update prices in text fields, while keeping the selected amount the same
                    if (btcInput.isFocused()) {
                        changeBtcPrice();
                    } else if (ethInput.isFocused()) {
                        changeEthPrice();
                    } else if (dollarInput.isFocused()) {
                        changeDollarPrice();
                    }

                    if (!oldBtcPrice.equals(btcPrice) || !oldEthPrice.equals(ethPrice)) {
                        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already up to date", Toast.LENGTH_LONG).show();
                    }
                    break;

                } catch (Exception e) {
                    System.out.println("REFRESH EXCEPTION: " + e);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrencyImg() {
        // Set currency symbol and flag icon
        currencyImg = findViewById(R.id.currencyImg);
        currencySymbol = findViewById(R.id.currencySymbol);
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

    }

    @SuppressLint("SetTextI18n")
    public void changeEthPrice() {
        // Only run if there is a number in the EditText
        if (!ethInput.getText().toString().equals("")) {

            // Get cost in dollars for ETH and format it: two decimal places & commas
            BigDecimal costInDollars = ethPrice.multiply(new BigDecimal(ethInput.getText().toString()));
            BigDecimal dollarNum = new BigDecimal(costInDollars.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String newDollar = decimalFormat.format(dollarNum);

            // Set the dollar input field with the dollar amount
            dollarInput.setText(newDollar);

            // Also update BTC amount depending on the amount of ETH entered
            BigDecimal amountOfBtc = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(btcPrice, 8, BigDecimal.ROUND_HALF_EVEN);
            btcInput.setText(amountOfBtc.toString());

        } else {
            dollarInput.setText("");
            btcInput.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void changeBtcPrice() {
        // Only run if there is a number in the EditText
        if (!btcInput.getText().toString().equals("")) {

            // Get cost in dollars for ETH and format it: two decimal places & commas
            BigDecimal costInDollars = btcPrice.multiply(new BigDecimal(btcInput.getText().toString()));
            BigDecimal dollarNum = new BigDecimal(costInDollars.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String newDollar = decimalFormat.format(dollarNum);

            // Set the dollar input field with the dollar amount
            dollarInput.setText(newDollar);

            // Also update ETH amount depending on the amount of BTC entered
            BigDecimal amountOfEth = new BigDecimal(newDollar.replaceAll(",", "")).divide(ethPrice, 8, BigDecimal.ROUND_HALF_EVEN);
            ethInput.setText(amountOfEth.toString());

        } else {
            dollarInput.setText("");
            ethInput.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    public void changeDollarPrice() {
        // Only run if there is a number in the EditText
        if (!dollarInput.getText().toString().replaceAll(",", "").equals("")) {

            // Get amount of BTC & ETH depending on dollars entered and format it: eight decimal places
            BigDecimal amountOfBtc = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(btcPrice, 8, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal amountOfEth = new BigDecimal(dollarInput.getText().toString().replaceAll(",", "")).divide(ethPrice, 8, BigDecimal.ROUND_HALF_EVEN);
            btcInput.setText(amountOfBtc.toString());
            ethInput.setText(amountOfEth.toString());

        } else {
            btcInput.setText("");
            ethInput.setText("");
        }
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
        // Go to settings activity
        if (motionEvent.getRawX() > motionEvent1.getRawX()) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
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


