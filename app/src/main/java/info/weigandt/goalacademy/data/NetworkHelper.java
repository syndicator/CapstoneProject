package info.weigandt.goalacademy.data;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NetworkHelper {
    public final static String FORISMATIC_COMPLETE_URL =  "http://api.forismatic.com/api/1.0/?method=getQuote&key=457653&format=json&lang=en";
    public final static String FORISMATIC_BASE_URL = "http://api.forismatic.com/api/1.0/";
    public final static String GET_PARAMETERS = "?method=getQuote&key=457653&format=json&lang=en";    // TODO check if ? has to be prefixed

    /**
     * Builds the URL used to query a random quote
     */
    public static URL buildQuoteUrl() {
        Uri builtUri = Uri.parse(FORISMATIC_BASE_URL).buildUpon()
                .build();
        URI correctedUri = null;
        try {
            correctedUri = new URI(builtUri.toString().replace("%2F", "/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            return correctedUri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Transform Uri to Url
     */
    private static URL transformToUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
