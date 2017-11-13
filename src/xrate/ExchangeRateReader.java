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
	
	private String givenURL;
    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     *
     * @param givenURL
     *            the base URL for requests
     * @throws IOException
     */
    public ExchangeRateReader(String givenURL) throws IOException {
      this.givenURL = givenURL;
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
    public float rateofExchange(String currencyCode, int year, int month, int day)
    throws ParserConfigurationException, SAXException, IOException{

      //Start an connection
      String URLaddress = createURLString(year, month, day);
      URL url = new URL(URLaddress);
      InputStream parsStrem = url.openStream();
      Document parsDoc = parseXMLDocument(parsStrem);
      NodeList exchangeRates = rateofExchangeList(parsDoc);
      float rate = rateofExchange(exchangeRates, currencyCode);

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
    public float rateofExchange(String donner, String acceptor,
      int year, int month, int day) throws IOException, ParserConfigurationException,
      SAXException {
        //New connection that has the full path name to the xml file that needs to be pars
        String URLaddress = createURLString(year, month, day);
        URL url = new URL(URLaddress);
        InputStream parsStrem = url.openStream();
        //Generates an xml Document
        Document parsDoc = parseXMLDocument(parsStrem);
        // Creates a list of exchange nodes for currency
        NodeList exchangeRates = rateofExchangeList(parsDoc);
        float rateCCurrency = rateofExchange(exchangeRates, donner);
        float pushCurrencyRate = rateofExchange(exchangeRates, acceptor);
        float rate = rateCCurrency / pushCurrencyRate;
        parsStrem.close();
        return rate;
    }

	private NodeList rateofExchangeList(Document parsDoc) {
    	return parsDoc.getElementsByTagName("fx");
	}
    
    private float rateofExchange(NodeList exchangeRates, String rateCCurrency) {
    	float exchangeRate = (float) 1.0;
    	for (int i =0; i < exchangeRates.getLength(); i++) {
    		Node mainNode = exchangeRates.item(i);
    		NodeList childNode = mainNode.getChildNodes();
    		String cString = childNode.item(1).getTextContent();
    		String rString = childNode.item(3).getTextContent();
    		float cRate = Float.parseFloat(rString);
    		if(cString.equals(rateCCurrency)) {
    			exchangeRate = cRate;
    			break;
    		}
    		
    	}
    	return exchangeRate;
	}

	private Document parseXMLDocument(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory buildMain = DocumentBuilderFactory.newInstance();
    	DocumentBuilder buildDoc = buildMain.newDocumentBuilder();
    	Document doc = buildDoc.parse(xmlStream);
    	doc.getDocumentElement().normalize();
    	
    	return doc;
    }
    private String createURLString(int year, int month, int day) {
 		String givenURL = this.givenURL;
 		String monthS = String.valueOf(month);
 		String dayS = String.valueOf(day);
 		if(monthS.length() == 1) {
 			monthS = "0" + monthS;
 		}
 		if(dayS.length() == 1) {
 			dayS = "0" + dayS;
 		}
 		String toReturn = givenURL + year + "/" + monthS + "/" + dayS + ".xml";
 		return toReturn;
 	}

}
