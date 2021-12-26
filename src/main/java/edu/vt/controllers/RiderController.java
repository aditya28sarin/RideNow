package edu.vt.controllers;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Rider;
import edu.vt.EntityBeans.RiderPhoto;
import edu.vt.FacadeBeans.RiderFacade;
import edu.vt.FacadeBeans.RideRiderFacade;
import edu.vt.FacadeBeans.RiderPhotoFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.json.*;

// Ride Controller
@Named("riderController")
@SessionScoped

public class RiderController implements Serializable {


    // Instance Variables

    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNo;
    private String gender;
    private Date dateOfBirth;
    private String interests;

    private String cellPhoneCarrier;

    private Map<String, Object> security_questions;
    private int securityQuestionNumber;
    private String answerToSecurityQuestion;

    private Rider selected;

    @EJB
    private RiderFacade riderFacade;

    @EJB
    private RideRiderFacade rideRiderFacade;

    @EJB
    private RiderPhotoFacade riderPhotoFacade;

    // Getter and Setter Methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo.replaceAll("[^0-9.]", "");
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public int getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public void setSecurityQuestionNumber(int securityQuestionNumber) {
        this.securityQuestionNumber = securityQuestionNumber;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void setAnswerToSecurityQuestion(String answerToSecurityQuestion) {
        this.answerToSecurityQuestion = answerToSecurityQuestion;
    }

    public String getCellPhoneCarrier() {
        return cellPhoneCarrier;
    }

    public void setCellPhoneCarrier(String cellPhoneCarrier) {
        this.cellPhoneCarrier = cellPhoneCarrier;
    }


    public Rider getSelected() {
        if (selected == null) {
            // Store the object reference of the signed-in User into the instance variable selected.
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            selected = (Rider) sessionMap.get("rider");
        }
        // Return the object reference of the selected (i.e., signed-in) User object
        return selected;
    }

    public void setSelected(Rider selected) {
        this.selected = selected;
    }

    // Check if Rider is Logged In

    public boolean isLoggedIn() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return sessionMap.get("rider") != null;
    }

    // Create Rider Account

    public String createAccount() {
        if (!password.equals(confirmPassword)) {
            Methods.showMessage("Fatal Error", "Unmatched Passwords!",
                    "Password and Confirm Password must Match!");
            return "";
        }

        Methods.preserveMessages();

        try {
            // Instantiate a new Rider object
            Rider newRider = new Rider();

            newRider.setName(name);
            newRider.setEmail(email);
            newRider.setPhoneNo(getPhoneNo());
            newRider.setGender(gender);
            newRider.setDateOfBirth(dateOfBirth);
            newRider.setInterest(interests);
            newRider.setSecurityQuestionNumber(securityQuestionNumber);
            newRider.setSecurityAnswer(answerToSecurityQuestion);
            newRider.setCellPhoneCarrier(cellPhoneCarrier);

            String parts = Password.createHash(password);
            newRider.setPassword(parts);

            // Create the rider in the database
            riderFacade.create(newRider);

        } catch (EJBException | Password.CannotPerformOperationException ex) {
            name = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating rider's account!",
                    "See: " + ex.getMessage());
            return "";
        }

        Methods.showMessage("Information", "Rider Account Created!",
                "");

        return "/riderAccount/SignIn?faces-redirect=true";
    }

    // Check if user is logged in or not

    public void notLoggedIn() {
        Methods.preserveMessages();

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

            String redirectPageURI = externalContext.getRequestContextPath() + "/riderAccount/SignIn.xhtml";

            externalContext.redirect(redirectPageURI);


        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Unable to redirect to the index (home) page!",
                    "See: " + ex.getMessage());
        }
    }

    // Logout User and Redirect to Show the Home Page

    public void logout() {

        // Clear the signed-in User's session map
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider r = (Rider)sessionMap.get("rider");
        String name = r.getName();
        sessionMap.clear();

        // Reset the signed-in User's properties
        email = password = confirmPassword = "";
        selected = null;

        // Since we will redirect to show the home page, invoke preserveMessages()
        Methods.preserveMessages();

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

            // Invalidate the signed-in User's session
            externalContext.invalidateSession();

            /*
            getRequestContextPath() returns the URI of the webapp directory of the application.
            Obtain the URI of the index (home) page to redirect to.
             */
            String redirectPageURI = externalContext.getRequestContextPath() + "/index.xhtml";

            Methods.showMessage("Information", "See you soon, " + name, "You have logged out!");
            // Redirect to show the index (home) page
            externalContext.redirect(redirectPageURI);

            /*
            NOTE: We cannot use: return "/index?faces-redirect=true"; here because the user's session is invalidated.
             */
        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Unable to redirect to the index (home) page!",
                    "See: " + ex.getMessage());
        }
    }

    //    Get the user Photo
    public String riderPhoto() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Integer primaryKey = (Integer) sessionMap.get("rider_id");

        List<RiderPhoto> photoList = riderPhotoFacade.findPhotosByRiderPrimaryKey(primaryKey);

        if (photoList.isEmpty()) {
            return Constants.PHOTOS_URI_RIDER + "defaultUserPhoto.png";
        }

        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        return Constants.PHOTOS_URI_RIDER + thumbnailFileName;
    }

    // Have Rated Function
    public boolean haveRated() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider rider = (Rider) sessionMap.get("rider");

        return rideRiderFacade.haveRated(rider);

    }

    // Get Security Functions for Rider

    public Map<String, Object> getSecurity_questions() {

        if (security_questions == null) {

            security_questions = new LinkedHashMap<>();

            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    //    Update the User Account Details
    public String updateAccount() {

        Methods.preserveMessages();


        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider editUser = (Rider) sessionMap.get("rider");

        try {

            editUser.setName(this.selected.getName());
            editUser.setEmail(this.selected.getEmail());
            editUser.setPhoneNo(this.selected.getPhoneNo());

            editUser.setCellPhoneCarrier(this.selected.getCellPhoneCarrier());
            editUser.setDateOfBirth(this.selected.getDateOfBirth());
            editUser.setInterest(this.selected.getInterest());
            editUser.setGender(this.selected.getGender());


            riderFacade.edit(editUser);

            Methods.showMessage("Information", "Your account has been Updated!",
                    "");

        } catch (EJBException ex) {
//            username = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while updating user's profile!",
                    "See: " + ex.getMessage());
            return "";
        }

        // Account update is completed, redirect to show the Profile page.
        return "/riderAccount/profile?faces-redirect=true";
    }


}
