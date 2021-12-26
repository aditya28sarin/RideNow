package edu.vt.EntityBeans;
import org.primefaces.shaded.json.JSONArray;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

// Searched News
public class SearchedNews {

    private String author;
    private String title;
    private String description;
    private String url;
    private String photoUrl;


    // Param Cosntructor
    public SearchedNews(String author, String title, String description, String url, String photoUrl) {

        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    // Getter and Setter Methods
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleLink() {
        String s = "<a href = "+url+">"+title+"</a>";
        return s;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
