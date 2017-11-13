package com.example.raksa.booksapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.raksa.booksapp.model.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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
        bookDataForTextView.execute(ApiUtility.buildUrl("Swimming"));

    }



    //For Getting The Data Of Book While Doing the work in the background
    public class SetBookDataForTextView extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {

            String result = null;
            try {
                result = ApiUtility.getJson(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
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
                ArrayList<Book> arrayListBooks = ApiUtility.getListOfBooks(result);

                StringBuffer stringBookData = new StringBuffer();

                if (arrayListBooks != null){

                    for (Book data : arrayListBooks){

                        stringBookData.append("Title : "+data.getTitle()+"\n") ;
                        stringBookData.append("SubTitle : "+data.getSubTitle()+"\n");
                        String authors = "Authors : ";
                        stringBookData.append(authors);
                        if (data.getAuthors()!=null){
                            StringBuffer appendStringBookData = new StringBuffer();
                            for (String author : data.getAuthors()){
                                appendStringBookData.append(author + " , ") ;
                            }

                            stringBookData.append(appendStringBookData.substring(0,appendStringBookData.length()-2));

                        }

                        stringBookData.append("\n");

                        stringBookData.append("Publisher : "+data.getPublisher()+"\n");
                        stringBookData.append("Publish Date : "+data.getPublisihedDate()+"\n");
                        stringBookData.append("\n\n");

                    }

                }

                textViewResponseApi.setText(stringBookData);
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
