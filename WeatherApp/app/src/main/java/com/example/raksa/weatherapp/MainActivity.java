package com.example.raksa.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    //view
    ImageView imageViewBackground;
    Button buttonGetTheWeather;
    EditText editTextLocationName;
    TextView textViewWeatherInfo , textViewWeatherDescription , textViewTempareter;
    LinearLayout linearLayoutWeatherContent;

    private int[] randomImageID = {R.drawable.night_sky,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.night_sky
            ,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get view reference
        imageViewBackground = findViewById(R.id.imageView);
        textViewTempareter = findViewById(R.id.textViewTempareter);
        buttonGetTheWeather = findViewById(R.id.buttonGetWeather);
        editTextLocationName = findViewById(R.id.editText);
        textViewWeatherInfo = findViewById(R.id.textViewWeather);
        textViewWeatherDescription = findViewById(R.id.textViewDescription);
        linearLayoutWeatherContent = findViewById(R.id.weatherContent);

        //set Random Background
        Random randomImage = new Random();
        imageViewBackground.setImageDrawable(getResources().getDrawable(randomImageID[randomImage.nextInt(7)]));

        //If ButtonGetTheWeather was click
        buttonGetTheWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWeatherDataForTheView();
            }
        });

    }

    public class SetWeatherDataForTheViewTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            final String BASE_URL_STRING = "http://api.openweathermap.org/data/2.5/weather";
            String query_data = editTextLocationName.getText().toString();

            Uri uri = Uri.parse(BASE_URL_STRING).buildUpon().appendQueryParameter("q",query_data).
                    appendQueryParameter("APPID",getResources().getString(R.string.apikey)).build();

            try {
                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\a");
                Boolean hasData = scanner.hasNext();
                if (hasData){
                    return scanner.next();
                }
                else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Make sure , you connect to internet!",Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (data!=null){
                Log.i("API Data",data);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data);
                    JSONArray weatherData = jsonObject.getJSONArray("weather");
                    JSONObject weather = weatherData.getJSONObject(0);
                    textViewWeatherInfo.setText(weather.getString("main"));
                    textViewWeatherDescription.setText(weather.getString("description"));

                    JSONObject tempareterData = jsonObject.getJSONObject("main");

                    float tempareter = Float.parseFloat(tempareterData.getString("temp"))-273.15f;
                    Log.i("tempareter data : ",tempareterData.getString("temp"));
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    decimalFormat.setRoundingMode(RoundingMode.FLOOR);
                    String tempareterString =decimalFormat.format(tempareter)+" °C";
                    textViewTempareter.setText(tempareterString);

                    if (linearLayoutWeatherContent.getVisibility()!= View.VISIBLE){
                        linearLayoutWeatherContent.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"City Not Found! Or Check Your Internet",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void setWeatherDataForTheView(){
            SetWeatherDataForTheViewTask weatherDataForTheViewTask = new SetWeatherDataForTheViewTask();
            weatherDataForTheViewTask.execute();
    }
}
