package edu.vt.controllers;

import edu.vt.EntityBeans.SearchedNews;
import edu.vt.globals.Methods;
import jdk.jfr.Name;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


// News API Controller

@SessionScoped
@Named("newsApiController")
public class NewsApiController implements Serializable {

    // Instance Variables

    private String searchApiURL;
    private List<SearchedNews> listOfNews;
    private int gridOrGallery = 0;

    // Getter and Setter Functions

    public int getGridOrGallery() {
        return gridOrGallery;
    }

    public void setGridOrGallery(int gridOrGallery) {
        this.gridOrGallery = gridOrGallery;
    }

    public String changegridOrGallery(int gridOrGallery) {
        this.gridOrGallery = gridOrGallery;
        return "/apiSearch/apiSearchResults?faces-redirect=true";
    }

    public String getSearchApiURL() {
        return searchApiURL;
    }

    public void setSearchApiURL(String searchApiURL) {
        this.searchApiURL = searchApiURL;
    }

    public List<SearchedNews> getListOfNews() {
        return listOfNews;
    }

    public void setListOfNews(List<SearchedNews> listOfNews) {
        this.listOfNews = listOfNews;
    }


    // Perform Search to Get Data from API

    public String performSearch() throws Exception {


        searchApiURL = "https://newsapi.org/v2/everything?q=ridesharing&from=2021-12-03&sortBy=publishedAt&apiKey=e5ba490a451d4332b9a31b8b00732cce";
        Methods.preserveMessages();


        try {

            listOfNews = new ArrayList<>();

            String searchResultsJsonData = Methods.readUrlContent(searchApiURL);

            JSONObject newsJSONObject = new JSONObject(searchResultsJsonData);

            JSONArray newsJSONArray = newsJSONObject.getJSONArray("articles");

            int i=0;

            if(newsJSONArray.length()<1){
                Methods.showMessage("Information", "No Results!", "No news articles found for RideNow!");
            }

            if(newsJSONArray.length()>i){
                while(i<newsJSONArray.length()){
                    JSONObject jsonObject = newsJSONArray.getJSONObject(i);

                    String author =  jsonObject.optString("author", "");
                    if(author.length()>20){
                        author= author.substring(0,20);
                    }

                    if (author.equals("")) {
                        i++;
                        continue;
                    }

                    String title = jsonObject.optString("title", "");
                    if(title.length()>20){
                        title= title.substring(0,20);
                    }
                    if (title.equals("")) {
                        i++;
                        continue;
                    }

                    String description = jsonObject.optString("description", "");
                    if(description.length()>50){
                        description= description.substring(0,50);
                    }
                    if (description.equals("")) {
                        i++;
                        continue;
                    }

                    String url = jsonObject.optString("url", "");
                    if (url.equals("")) {
                        i++;
                        continue;
                    }

                    String photoUrl = jsonObject.optString("urlToImage", "");
                    if (photoUrl.equals("")) {
                        i++;
                        continue;
                    }


                    SearchedNews recipe = new SearchedNews(author, title, description, url, photoUrl);

                    listOfNews.add(recipe);
                    i++;

                }
            }else{

            }


        } catch (IOException ex) {
            Methods.showMessage("Fatal Error", "Error accessing Edamam Recipe Search API",
                    "See: " + ex.getMessage());
            searchApiURL = "";
            return "";

        }

        return "/apiSearch/apiSearchResults?faces-redirect=true";
    }

}
