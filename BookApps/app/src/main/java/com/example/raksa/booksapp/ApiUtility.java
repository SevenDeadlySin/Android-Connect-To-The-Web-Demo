package com.example.raksa.booksapp;

import android.net.Uri;
import android.util.Log;

import com.example.raksa.booksapp.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Raksa on 10/30/2017.
 */

public class ApiUtility {

    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    private static final String API_KEY = "AIzaSyDrE6XL_d33BZ8aEI6CIbtNshdKpoYnlSk";

    public static URL buildUrl(String title) {

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (Exception e){
            Log.d("Error", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }
    }

    public static ArrayList<Book> getListOfBooks(String JSONString){

        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE ="publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";

        ArrayList<Book> arrayListBooks = new ArrayList<Book>();

        try {
            JSONObject jsonObjectData = new JSONObject(JSONString);
            int jsonArrayItemLength = jsonObjectData.getJSONArray(ITEMS).length();
            for (int i = 0 ; i < jsonArrayItemLength ; i++ ){
                JSONObject jsonObjectItem= jsonObjectData.getJSONArray(ITEMS).getJSONObject(i);
                JSONObject jsonObjectVolumeInfo = jsonObjectItem.getJSONObject(VOLUMEINFO);

                JSONArray jsonArrayAuthors = jsonObjectVolumeInfo.optJSONArray(AUTHORS);

                ArrayList<String> authors = new ArrayList<String>();
                if (jsonArrayAuthors!=null){
                    for (int j = 0 ; j < jsonArrayAuthors.length();j++){
                        authors.add(jsonObjectVolumeInfo.getJSONArray(AUTHORS).optString(j));
                    }

                }
                else {
                    authors.add("Unknown");
                }

                arrayListBooks.add(new Book(jsonObjectVolumeInfo.getString(TITLE),jsonObjectVolumeInfo.optString(SUBTITLE,""),
                        authors,jsonObjectVolumeInfo.optString(PUBLISHER,"Unknown"),jsonObjectVolumeInfo.optString(PUBLISHED_DATE,"Unknown")));
            }
            if (arrayListBooks.size()>0){
                return arrayListBooks;
            }
            else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
