package cryptoconverter.com;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testBtcPrice() throws Exception {
        // Test that Bitcoin prices can be set and retrieved properly
        RetrieveCryptoPrices retrieveCryptoPrices = new RetrieveCryptoPrices();
        retrieveCryptoPrices.setBtcPrice("10000");
        assertEquals("10000", retrieveCryptoPrices.getBtcPrice());

    }

    @Test
    public void testEthPrice() throws Exception {
        // Test that Ethereum prices can be set and retrieved properly
        RetrieveCryptoPrices retrieveCryptoPrices = new RetrieveCryptoPrices();
        retrieveCryptoPrices.setEthPrice("800");
        assertEquals("800", retrieveCryptoPrices.getEthPrice());

    }

    @Test
    public void testApiCall() throws Exception {
        // Test API call is working and returning a string value
        RetrieveCryptoPrices retrieveCryptoPrices = new RetrieveCryptoPrices();
        retrieveCryptoPrices.retrieveCryptoPrice("btc", "aud");
        assertEquals(String.class.getSimpleName(), retrieveCryptoPrices.retrieveCryptoPrice("btc", "aud").getClass().getSimpleName());
    }

}