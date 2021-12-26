package edu.vt.controllers;

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.CabFacade;
import edu.vt.FacadeBeans.DriverFacade;
import edu.vt.FacadeBeans.DriverPhotoFacade;
import edu.vt.FacadeBeans.RideFacade;
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


//This is the Driver Controller
@Named("driverController")
@SessionScoped
public class DriverController implements Serializable {

    // Instance Variables
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNo;
    private String gender;
    private Date dateOfBirth;
    private String cities;
    private String interests;

    private Map<String, Object> security_questions;
    private int securityQuestionNumber;
    private String answerToSecurityQuestion;

    // Cab Details
    private String brand = "";
    private String model = "";
    private String registrationNumber;
    private int type;
    private int capacity;

    private Driver selected;

    private List<String> listOfMakes;

    @EJB
    private DriverFacade driverFacade;

    @EJB
    private CabFacade cabFacade;

    @EJB
    private DriverPhotoFacade driverPhotoFacade;

    @EJB
    private RideFacade rideFacade;

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

    public void setPhoneNo(String cellPhoneNo) {
        this.phoneNo = cellPhoneNo.replaceAll("[^0-9.]", "");
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

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
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

    public Driver getSelected() {
        if (selected == null) {
            // Store the object reference of the signed-in User into the instance variable selected.
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            selected = (Driver) sessionMap.get("driver");
        }
        // Return the object reference of the selected (i.e., signed-in) User object
        return selected;
    }

    public void setSelected(Driver selected) {
        this.selected = selected;
    }



    // Check if the Driver is Logged In
    public boolean isLoggedIn() {
        /*
        The username of a signed-in user is put into the SessionMap in the
        initializeSessionMap() method in LoginManager upon user's sign in.
        If there is a username, that means, there is a signed-in user.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return sessionMap.get("driver") != null;
    }

    // Cab Details Getter and Setter
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    // Get List of Car Makes from API
    public List<String> getListOfMakes() throws IOException, InterruptedException {
        if (listOfMakes == null) {
            listOfMakes = new ArrayList<String>();
            listOfMakes = cabFacade.getMakesFromAPI();
            brand = listOfMakes.get(0);
        }
        return listOfMakes;
    }

    public void setListOfMakes(List<String> listOfMakes) {
        this.listOfMakes = listOfMakes;
    }


    // Create Driver Account and Redirect SignIn Page
    public String createAccount() {
        if (!password.equals(confirmPassword)) {
            Methods.showMessage("Fatal Error", "Unmatched Passwords!",
                    "Password and Confirm Password must Match!");
            return "";
        }

        Methods.preserveMessages();

        try {

            // Instantiate a new Driver object
            Driver newDriver = new Driver();

            newDriver.setName(name);
            newDriver.setEmail(email);
            newDriver.setPhoneNo(getPhoneNo());
            newDriver.setGender(gender);
            newDriver.setDateOfBirth(dateOfBirth);
            newDriver.setInterests(interests);
            newDriver.setSecurityQuestionNumber(securityQuestionNumber);
            newDriver.setSecurityAnswer(answerToSecurityQuestion);
            newDriver.setCities(cities);

            // create and save cab object
            Cab cab = new Cab();
            cab.setBrand(brand);
            cab.setModel(model);
            cab.setRegistrationNumber(registrationNumber);
            cab.setType(type);
            cab.setCapacity(capacity);

            // Create the driver in the database
            cabFacade.create(cab);

            newDriver.setCabId(cab);

            String parts = Password.createHash(password);
            newDriver.setPassword(parts);

            // Create the driver in the database
            driverFacade.create(newDriver);

        } catch (EJBException | Password.CannotPerformOperationException ex) {
            name = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating driver's account!",
                    "See: " + ex.getMessage());
            return "";
        }

        Methods.showMessage("Information", "Driver Account Created!",
                "");

        return "/driverAccount/SignIn?faces-redirect=true";
    }


    public void notLoggedIn() {
        Methods.preserveMessages();

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            /*
            getRequestContextPath() returns the URI of the webapp directory of the application.
            Obtain the URI of the index (home) page to redirect to.
             */
            String redirectPageURI = externalContext.getRequestContextPath() + "/driverAccount/SignIn.xhtml";

            // Redirect to show the index (home) page

            externalContext.redirect(redirectPageURI);

            /*
            NOTE: We cannot use: return "/index?faces-redirect=true"; here because the user's session is invalidated.
             */
        } catch (IOException ex) {
            Methods.showMessage("Error",
                    "Unable to redirect to the index (home) page!",
                    "See: " + ex.getMessage());
        }


    }

    // Logout User and Redirect to Show the Home Page

    public void logout() {

        // Clear the signed-in User's session map
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Driver d = (Driver)sessionMap.get("driver");
        String name = d.getName();
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

            // Redirect to show the index (home) page
            Methods.showMessage("Information", "See you soon, " + name, "You have logged out!");
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

    // Upload Photo for Driver
    public String driverPhoto() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Integer primaryKey = (Integer) sessionMap.get("driver_id");

        List<DriverPhoto> photoList = driverPhotoFacade.findPhotosByDriverPrimaryKey(primaryKey);

        if (photoList.isEmpty()) {
            return Constants.PHOTOS_URI_DRIVER + "defaultUserPhoto.png";
        }

        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        return Constants.PHOTOS_URI_DRIVER + thumbnailFileName;
    }

    // Upload Photo for Driver
    public String driverPhoto(Integer primaryKey) {

        List<DriverPhoto> photoList = driverPhotoFacade.findPhotosByDriverPrimaryKey(primaryKey);

        if (photoList.isEmpty()) {
            return Constants.PHOTOS_URI_DRIVER + "defaultUserPhoto.png";
        }

        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        return Constants.PHOTOS_URI_DRIVER + thumbnailFileName;
    }

    // Disable Model
    public boolean disableModel(String selectedMake) {
        if (this.getModel().equalsIgnoreCase("Other") || selectedMake.equalsIgnoreCase("Other")) {
            return false;
        }
        return true;
    }

    // Get models for the particular make of Car
    public List<String> getModelsForMake(String make) throws IOException, InterruptedException {
        List<String> models = new ArrayList<String>();
        models = cabFacade.getModelsForMake(make);
        model = models.get(0);
        return models;
    }

    // Check if Make is Selected
    public boolean isMakeSelected() {
        if (this.brand.equalsIgnoreCase("Other")) return true;
        return false;
    }


    // Get Security Questions for User
    public Map<String, Object> getSecurity_questions() {

        if (security_questions == null) {

            security_questions = new LinkedHashMap<>();

            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    // Enum CabType
    public enum CabType {
        Hatchback,
        Sedan,
        SUV
    }

    // Update Account for Driver
    public String updateAccount() {

        Methods.preserveMessages();


        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Driver editUser = (Driver) sessionMap.get("driver");

        try {

            editUser.setName(this.selected.getName());
            editUser.setEmail(this.selected.getEmail());
            editUser.setPhoneNo(this.selected.getPhoneNo());

            editUser.setCities(this.selected.getCities());
            editUser.setDateOfBirth(this.selected.getDateOfBirth());
            editUser.setInterests(this.selected.getInterests());
            editUser.setGender(this.selected.getGender());


            driverFacade.edit(editUser);

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
        return "/driverAccount/profile?faces-redirect=true";
    }

    // Get back Driver Car Type
    public String getDriverCarType(int type) {
        return Constants.CabType.values()[type].name();
    }

}
