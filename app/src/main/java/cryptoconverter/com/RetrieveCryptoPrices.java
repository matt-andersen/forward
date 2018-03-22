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

    @Override
    protected String doInBackground(String[] params) {

        String currency = MainActivity.currency;

        try {
            // Retrieve crypto price and set
            setBtcPrice(retrieveCryptoPrice("btc", currency));
            setEthPrice(retrieveCryptoPrice("eth", currency));

        } catch (Exception e) {

            switch (currency) {
                case("aud"):
                    setBtcPrice("11913.28");
                    setEthPrice("759.72");
                    break;
                case("usd"):
                    setBtcPrice("9077.29");
                    setEthPrice("574.54");
                    break;
                case("gbp"):
                    setBtcPrice("6623.59");
                    setEthPrice("425.87");
                    break;
            }

        }
        return null;
    }

    void setBtcPrice(String btcPrice) {
        this.btcPrice = btcPrice;
    }

    String getBtcPrice() {
        return btcPrice;
    }

    void setEthPrice(String ethPrice) {
        this.ethPrice = ethPrice;
    }

    String getEthPrice() {
        return ethPrice;
    }

    String retrieveCryptoPrice(String crypto, String currency) throws Exception{

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
