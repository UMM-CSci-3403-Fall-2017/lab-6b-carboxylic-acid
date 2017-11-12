package xrate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Provide access to basic currency exchange rate services.
 *
 * @author carboxylic acid
 */
public class ExchangeRateReader {

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     *
     * @param baseURL
     *            the base URL for requests
     * @throws IOException
     */
    public ExchangeRateReader(String baseURL) throws IOException {
      this.baseURL = baseURL;
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     *
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day)
    throws ParserConfigurationException, SAXException, IOException{

      //Start an connection
      String URLaddress = buildURLString(year, month, day);
      URL url = new URL(urlsting);
      InputStream parsStrem = url.openStream();
      Document parsDoc = createXMLDocument(parsStrem);
      NodeList exchangeRates = getExchangeRateList(xmlDoc);
      float rate = getExchangeRate(exchangeRates, currencyCode);

      parsStrem.close();
      return rate;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     *
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(String fromCurrency, String toCurrency,
      int year, int month, int day) throws IOException, ParserConfigurationException,
      SAXException {
        //New connection that has the full path name to the xml file that needs to be pars
        String URLaddress = buildURLString(year, month, day);
        URL url = new URL(urlsting);
        InputStream parsStrem = url.openStream();
        //Generates an xml Document
        Document parsDoc = createXMLDocument(parsStrem);
        // Creates a list of exchange nodes for currency
        NodeList exchangeRates = getExchangeRateList(parsDoc);
        float rateCCurrency = getExchangeRate(exchangeRates, rateCCurrency);
        float pushCurrencyRate = getExchangeRate(exchangeRates, pushCurrencyRate);
        float rate = rateCCurrency / pushCurrencyRate;
        parsStrem.close();
        return rate;
    }
}
