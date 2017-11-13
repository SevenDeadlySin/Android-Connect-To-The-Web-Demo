package com.example.raksa.booksapp.model;

import java.util.ArrayList;

/**
 * Created by Raksa on 11/9/2017.
 */

public class Book {

    private String title;
    private String subTitle;
    private ArrayList<String> authors;
    private String publisher;
    private String publisihedDate;

    public Book(String title, String subTitle, ArrayList authors, String publisher, String publisihedDate) {
        this.title = title;
        this.subTitle = subTitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publisihedDate = publisihedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisihedDate() {
        return publisihedDate;
    }

    public void setPublisihedDate(String publisihedDate) {
        this.publisihedDate = publisihedDate;
    }
}
