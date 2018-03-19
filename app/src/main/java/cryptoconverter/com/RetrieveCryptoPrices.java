package cryptoconverter.com;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RetrieveCryptoPrices extends AsyncTask<String, Void, String> {

    // Create global vars for crypto prices and get the chosen federal currency from shared prefs in Main
    private String btcPrice = "";
    private String ethPrice = "";
    private String currency = MainActivity.currency;

    @Override
    protected String doInBackground(String[] params) {

        try {
            // Retrieve crypto price and set
            setBtcPrice(retrieveCryptoPrice("btc"));
            setEthPrice(retrieveCryptoPrice("eth"));

        } catch (Exception ignored) {
        }
        return null;
    }

    private void setBtcPrice(String btcPrice) {
        this.btcPrice = btcPrice;
    }

    double getBtcPrice() {
        return Double.parseDouble(btcPrice);
    }

    private void setEthPrice(String ethPrice) {
        this.ethPrice = ethPrice;
    }

    String getEthPrice() {
        return ethPrice;
    }

    private String retrieveCryptoPrice(String crypto) throws Exception{

        // Set the URL for API call using the crypto parameter
        String url = "https://api.cryptonator.com/api/ticker/" + crypto + "-" + currency;

        URL obj = new URL(url);

        // Create HTTP GET connection
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        // Make HTTP request header, get response, and close connection
        con.addRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Read JSON response and return crypto price
        JSONObject myResponse = new JSONObject(response.toString());
        return myResponse.getJSONObject("ticker").getString("price");
    }
}
