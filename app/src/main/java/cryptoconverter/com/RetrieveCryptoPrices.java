package cryptoconverter.com;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RetrieveCryptoPrices extends AsyncTask<String, Void, String> {

    public String btcPrice = "";
    public String ethPrice = "";
    String currency = MainActivity.currency;

    @Override
    protected String doInBackground(String[] params) {

        try {
            setBtcPrice(retrieveCryptoPrice("btc"));
            setEthPrice(retrieveCryptoPrice("eth"));

        } catch (Exception e) {
            System.out.println("UH OH EXCEPTO" + e);
        }

        return null;
    }

    public void setBtcPrice(String btcPrice) {
        this.btcPrice = btcPrice;
    }

    public double getBtcPrice() {
        return Double.parseDouble(btcPrice);
    }

    public void setEthPrice(String ethPrice) {
        this.ethPrice = ethPrice;
    }

    public String getEthPrice() {
        return ethPrice;
    }

    public String retrieveCryptoPrice(String crypto) throws Exception{
        String url = "https://api.cryptonator.com/api/ticker/" + crypto + "-" + currency;
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Optional default is GET
        con.setRequestMethod("GET");

        // Add request header
        con.addRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\n Sending 'GET' request to URL : " + url);
        System.out.println("Response code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Read JSON response and set BTC price
        JSONObject myResponse = new JSONObject(response.toString());
        return myResponse.getJSONObject("ticker").getString("price");
    }
}
