package com.example.raksa.booksapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Raksa on 10/30/2017.
 */

public class ApiUtility {

    private static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    public static URL buildURL(String bookTitle){

        //using Purely URL with String
//        String fullURL = BASE_API_URL + "?q=" + bookTitle;
//        try {
//            URL url = new URL(fullURL);
//            return url;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }

        //using Uri
        Uri uri = Uri.parse(BASE_API_URL).buildUpon().appendQueryParameter("q",bookTitle).build();
        try {
            URL url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJSON(URL bookApiURL) throws IOException {

        HttpsURLConnection urlConnection = null;
        InputStream urlStream = null;

        try {
            urlConnection = (HttpsURLConnection) bookApiURL.openConnection();
            urlStream = urlConnection.getInputStream();
            Scanner dataScanner = new Scanner(urlStream);
            dataScanner.useDelimiter("\\A");

            Boolean hasData = dataScanner.hasNext();
            if (hasData){
                return dataScanner.next();
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (urlStream!=null){
                urlStream.close();
            }
        }
    }

}
