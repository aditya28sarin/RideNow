package edu.vt.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

// Slider Controller

@Named("sliderController")
@RequestScoped
public class SliderController {

    // The List contains image filenames, e.g., photo1.png, photo2.png, etc.
    private List<String> listOfSliderImageFilenames;

    @PostConstruct
    public void init() {
        listOfSliderImageFilenames = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            listOfSliderImageFilenames.add("photo" + i + ".png");
        }
    }

    // Getter Method
    public List<String> getListOfSliderImageFilenames() {
        return listOfSliderImageFilenames;
    }

    // Instance Methods
    public String description(String imageFilename) {

        String imageDescription = "Ride Now App";

        switch (imageFilename) {
            case "photo1.png":
                //imageDescription = "";
                break;
            case "photo2.png":
                //imageDescription = "X-Men: Apocalypse";
                break;
            case "photo3.png":
                //imageDescription = "The Hunger Games: Catching Fire";
                break;
            case "photo4.png":
                //imageDescription = "Captain America: Civil War";
                break;
            case "photo5.png":
                //imageDescription = "Batman v Superman: Dawn of Justice";
                break;
            case "photo6.png":
                //imageDescription = "Iron Man 3";
                break;
            case "photo7.png":
                //imageDescription = "Dawn of the Planet of the Apes";
                break;
            case "photo8.png":
                //imageDescription = "Live Die Repeat: Edge of Tomorrow";
                break;
        }

        return imageDescription;
    }


}
