package com.example.raksa.booksapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

     TextView textViewResponseApi,textViewAlert;
     ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResponseApi = findViewById(R.id.tvResponseAPI);
        mProgressBar = findViewById(R.id.progressBar);
        textViewAlert = findViewById(R.id.textViewAlert);


        SetBookDataForTextView bookDataForTextView = new SetBookDataForTextView();
        //get the book data from the internet(Background Threads).....
        bookDataForTextView.execute(ApiUtility.buildURL("Leonardo da Vinci"));

    }



    //For Getting The Data Of Book While Doing the work in the background
    @SuppressLint("StaticFieldLeak")
    public class SetBookDataForTextView extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            try {
                return ApiUtility.getJSON(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result==null){
                textViewAlert.setVisibility(View.VISIBLE);
                if (mProgressBar.getVisibility()!=View.INVISIBLE){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            }
            else {
                textViewResponseApi.setText(result);
                if (mProgressBar.getVisibility()!=View.INVISIBLE){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            if (mProgressBar.getVisibility()!= View.VISIBLE){
                mProgressBar.setVisibility(View.VISIBLE);
                textViewAlert.setVisibility(View.INVISIBLE);
            }
        }

    }


}
