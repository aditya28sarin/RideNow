package edu.vt.controllers;

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.*;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.event.RateEvent;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.mail.MessagingException;

// Ride Controller

@Named("rideController")
@SessionScoped
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RideController implements Serializable {

    // Instance Variables (Properties)

    private Driver driver;
    private List<Rider> riders;
    private Cab cab;
    private Date startTime;
    private Location startLocation;
    private Location endLocation;

    private String staticMapImageUrl;

    private String startDestinationDistance;

    private String startDestinationEstimatedTime;

    private Ride selected;

    private Rider rider;
    private String startLocationString;
    private String endLocationString;
    private List<Driver> listOfDrivers = null;

    private List<Rider> listOfRiders = null;

    private List<Ride> listOfAvailableRides = null;

    private List<Ride> listOfRiderHistory = null;

    private String findRideStartLocation, findRideEndLocation;
    private Date findRideStartTime;

    private int driverRating;

    private float estimatedRideFare;

    private String rideType;

    private String selectedRowNumber = "0";

    @Inject
    private EmailController emailController;

    @Inject
    private TextMessageController textMessageController;

    @EJB
    private RideFacade rideFacade;

    @EJB
    private LocationFacade locationFacade;

    @EJB
    private DriverFacade driverFacade;

    @EJB
    private RiderFacade riderFacade;

    @EJB
    private RideRiderFacade rideRiderFacade;

    // Sends Email and Text Message to the Rider

    private boolean sendEmailAndTextMessage(Rider rider, String subject, String message) throws MessagingException {
        emailController.setEmailTo(rider.getEmail());
        emailController.setEmailSubject(subject);
        emailController.sendEmail("Hi " + rider.getName() + "! " + message);

        // Send Join Ride Confirmation text SMS to the Rider
        textMessageController.setCellPhoneNumber(rider.getPhoneNo());
        textMessageController.setCellPhoneCarrierDomain(rider.getCellPhoneCarrier());
        textMessageController.setMmsTextMessage("Hi " + rider.getName() + "! " + message);

        textMessageController.sendTextMessage();

        return true;
    }

    // Getter and Setter Methods

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Rider> getRiders() {
        return riders;
    }

    public void setRiders(List<Rider> riders) {
        this.riders = riders;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Ride getSelected() {
        return selected;
    }

    public Ride getTheSelected() {
        Ride x = selected;
        return selected;
    }

    public void setSelected(Ride selected) {
        this.selected = selected;
    }

    public List<Ride> getListOfRiderHistory() {
        if (listOfRiderHistory == null) {
            listOfRiderHistory = getRiderHistory();
        }
        return listOfRiderHistory;
    }

    public void setListOfRiderHistory(List<Ride> listOfRiderHistory) {
        this.listOfRiderHistory = listOfRiderHistory;
    }

    public void clearRiderHistory() {
        listOfRiderHistory = null;
        selected = null;
        driverRating = 0;
    }

    public String getStartLocationString() {
        return startLocationString;
    }

    public void setStartLocationString(String startLocationString) {
        this.startLocationString = startLocationString;
    }

    public String getEndLocationString() {
        return endLocationString;
    }

    public void setEndLocationString(String endLocationString) {
        this.endLocationString = endLocationString;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public String getStaticMapImageUrl() {
        return staticMapImageUrl;
    }

    public void setStaticMapImageUrl(String staticMapImageUrl) {
        this.staticMapImageUrl = staticMapImageUrl;
    }

    public String getStartDestinationDistance() {
        return startDestinationDistance;
    }

    public void setStartDestinationDistance(String startDestinationDistance) {
        this.startDestinationDistance = startDestinationDistance;
    }

    public String getStartDestinationEstimatedTime() {
        return startDestinationEstimatedTime;
    }

    public void setStartDestinationEstimatedTime(String startDestinationEstimatedTime) {
        this.startDestinationEstimatedTime = startDestinationEstimatedTime;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public List<Ride> getListOfAvailableRides() {
        if (listOfAvailableRides != null) {
            return listOfAvailableRides;
        }
        return findAvailableRides();
    }

    public void setListOfAvailableRides(List<Ride> listOfAvailableRides) {
        this.listOfAvailableRides = listOfAvailableRides;
    }

    public String getFindRideStartLocation() {
        return findRideStartLocation;
    }

    public void setFindRideStartLocation(String findRideStartLocation) {
        this.findRideStartLocation = findRideStartLocation;
    }

    public String getFindRideEndLocation() {
        return findRideEndLocation;
    }

    public void setFindRideEndLocation(String findRideEndLocation) {
        this.findRideEndLocation = findRideEndLocation;
    }

    public Date getFindRideStartTime() {
        return findRideStartTime;
    }

    public void setFindRideStartTime(Date findRideStartTime) {
        this.findRideStartTime = findRideStartTime;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
    }

    public EmailController getEmailController() {
        return emailController;
    }

    public void setEmailController(EmailController editorController) {
        this.emailController = editorController;
    }

    public TextMessageController getTextMessageController() {
        return textMessageController;
    }

    public void setTextMessageController(TextMessageController textMessageController) {
        this.textMessageController = textMessageController;
    }

    public int getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }

    public int getRatingInt(float rating) {
        return (int) rating;
    }

    public int getRatingInt(Ride currRide) {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider currRider = (Rider) sessionMap.get("rider");

        int rating = rideRiderFacade.getRating(currRide, currRider);
        return rating;
    }

    public float getEstimatedRideFare() {
        return estimatedRideFare;
    }

    public float getCustomRideFare() {
        return estimatedRideFare;
    }

    public void setEstimatedRideFare(float estimatedRideFare) {
        this.estimatedRideFare = estimatedRideFare;
    }

    public void unselect() {
        driverRating = 0;
        selected = null;
    }

    // On Rate Function

    public void onRate(RateEvent<Integer> rateEvent) throws IOException {
        String driverName = rateDriver();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "You Rated " + driverName + " ", "Rating:" + rateEvent.getRating());
        FacesContext.getCurrentInstance().addMessage(null, message);
        selected = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);

        FacesContext.getCurrentInstance().getExternalContext().redirect("listRiderRides.xhtml");
    }

    public String redirect() {
        return "/riderRidesHistor/listRiderRides?faces-redirect=true";
    }

    // On rate Cancel Function

    public void onRatecancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Rating", "Rate Reset");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // Create a Ride

    public String createRide() {

        Methods.preserveMessages();

        try {
            // Instantiate a new Ride object
            Ride newRide = new Ride();

            driver = (Driver) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("driver");

            newRide.setDriverID(driver);
            newRide.setStatus(Constants.RideStatus.NotStarted.ordinal());
            newRide.setSeats(driver.getCabId().getCapacity());
            newRide.setStartTime(startTime);

            //riders = new ArrayList<Rider>();

            newRide.setTripStartTime(new Timestamp(startTime.getTime()));

            // create and save start Location object
            Location startLocation = new Location();
            startLocation.setLatitude("40.70714");
            startLocation.setLongitude("-74.01086");
            startLocation.setLandmarkName(startLocationString);
            startLocation.setZipCode(24061);

            // save the start Location in the database
            locationFacade.create(startLocation);

            // create and save destination Location object
            Location destinationLocation = new Location();
            destinationLocation.setLatitude("-74.01205");
            destinationLocation.setLongitude("45.70804");
            destinationLocation.setLandmarkName(endLocationString);
            destinationLocation.setZipCode(24060);

            // save the destination Location in the database
            locationFacade.create(destinationLocation);

            newRide.setStartLocation(startLocation);
            newRide.setDestinationLocation(destinationLocation);


            // Google static map api
            String imageUrl = locationFacade.generateStaticMapImageUrl(startLocationString, endLocationString);
            setStaticMapImageUrl(imageUrl);

            // Google Distance matrix api
            ArrayList<String> distance = locationFacade.calculateDistance(startLocationString, endLocationString);
            setStartDestinationDistance(distance.get(0));
            setStartDestinationEstimatedTime(distance.get(1));

            newRide.setDistance(Float.parseFloat(distance.get(0).split(" ")[0].replace(",", "")));

            // save ride
            rideFacade.create(newRide);

            setSelected(newRide);

        } catch (EJBException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating ride!",
                    "See: " + ex.getMessage());
            return "";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Methods.preserveMessages();

        Methods.showMessage("Information", "Ride Created",
                "");

        return "/ride/view?faces-redirect=true";
    }

    // Get Ride Status Function

    public String getRideStatus(int rideStatusValue) {

        return Constants.RideStatus.values()[rideStatusValue].name();
    }

   // Instance Methods

    public List<Ride> findAvailableRides() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        if (sessionMap.get("rider") != null) {
            Rider currRider = (Rider) sessionMap.get("rider");
            int riderid = currRider.getId();
            listOfAvailableRides = rideFacade.findAvailableRides(findRideStartLocation, findRideEndLocation, findRideStartTime, riderid);
        } else {
        }
        findRideStartTime=null;
        findRideEndLocation=null;
        findRideStartLocation=null;
        return listOfAvailableRides;
    }

    // Join Ride Function

    public String joinRide() throws MessagingException {

        Methods.preserveMessages();

        selected.setSeats(selected.getSeats() - 1);
        rideFacade.edit(selected);

        // create entry in rider - ride mapping table

        rider = (Rider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rider");

        RideRider rideRider1 = new RideRider();
        rideRider1.setRideId(selected);
        rideRider1.setRiderId(rider);

        rideRiderFacade.create(rideRider1);

        String joinRideMessage = "You have successfully joined the ride from " + selected.getStartLocation().getLandmarkName() +
                " to " + selected.getDestinationLocation().getLandmarkName() +
                ". Ride confirmation E-mail and SMS has been sent to you! Enjoy your ride!";

        String joinEmailRideMessage = "You have successfully joined the ride from " + selected.getStartLocation().getLandmarkName() +
                " to " + selected.getDestinationLocation().getLandmarkName() +
                ". Enjoy your ride!";

        // Send Join Ride Confirmation E-Mail and Text SMS to the Rider
        sendEmailAndTextMessage(rider, "Ride Confirmed!", joinEmailRideMessage);

        // show Join Ride Confirmation Message
        Methods.showMessage("Information", "Success!", joinRideMessage);

        listOfAvailableRides = null;
        selected = null;

        return "/riderRidesHistory/listRiderRides?faces-redirect=true";
    }

    // Cancel Ride Function

    public void cancelRide() throws MessagingException {

        Methods.preserveMessages();

        selected.setSeats(selected.getSeats() + 1);
        rideFacade.edit(selected);

        // create entry in rider - ride mapping table

        rider = (Rider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rider");

        rideRiderFacade.removeRideRider(selected, rider);

        listOfRiderHistory = null;
        selected = null;
        Methods.showMessage("Information", "Ride Cancelled", "");
    }

    // Rating Driver Function

    public String rateDriver() {
        rider = (Rider) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rider");

        rideRiderFacade.updateDriverRating(getDriverRating(), selected, rider);

        // calculate and save overall driver rating for that ride
        CalculateAndSaveOverallRating();

        //Update driver's rating
        Driver driver = selected.getDriverID();
        float rating = driverFacade.getAvgRating(driver);
        driver.setOverallRating(rating);
        driverFacade.edit(driver);
        return driver.getName();
    }


    // Calculate and save overall rating for driver

    public void CalculateAndSaveOverallRating() {
        Ride rideToEnd = selected;

        float averageRating = (float) rideRiderFacade.calculateAverageRating(rideToEnd.getId());

        rideToEnd.setDriverRating(averageRating);
        rideFacade.edit(rideToEnd);

    }

    // Get Rider History

    public List<Ride> getRiderHistory() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider rider = (Rider) sessionMap.get("rider");
        if (rider != null) {
            listOfRiderHistory = rideFacade.findHistoryOfRider(rider);
        } else {
        }
        return listOfRiderHistory;
    }

    // Search Available Rides

    public String searchAvailableRides() {
        listOfAvailableRides = null;
        return "/FindRides/ViewAvailableRides?faces-redirect=true";
    }

    // Get All Drivers

    public List<Driver> getAllDrivers() {
        if (listOfDrivers == null) {
            listOfDrivers = driverFacade.findAll();
        }

        return listOfDrivers;
    }

    // Get All Driver Names

    public List<String> getAllDriverNames() {
        List<String> driverNames = new ArrayList<>();

        if (listOfDrivers == null) {
            listOfDrivers = driverFacade.findAll();
        }

        for (Driver driver : listOfDrivers) {
            driverNames.add(driver.getName());
        }

        return driverNames;
    }

    // Get all Riders

    public List<Rider> getAllRiders() {
        if (listOfRiders == null) {
            listOfRiders = riderFacade.findAll();
        }

        return listOfRiders;
    }

    // Get all rider names

    public List<String> getAllRiderNames() {
        List<String> riderNames = new ArrayList<>();

        if (listOfRiders == null) {
            listOfRiders = riderFacade.findAll();
        }

        for (Rider rider : listOfRiders) {
            riderNames.add(rider.getName());
        }

        return riderNames;
    }


    // get all rider names in ride

    public List<String> getAllRiderNamesInRide() {
        List<String> riderNames = new ArrayList<>();

        if (riders != null) {
            for (Rider rider : riders) {
                riderNames.add(rider.getName());
            }
        }

        return riderNames;
    }

    // Update the selected ride

    public String updateRide() {

        Methods.preserveMessages();

        Ride rideToUpdate = selected;
        rideToUpdate.setFare(getEstimatedRideFare());
        rideFacade.edit(rideToUpdate);

        this.startLocationString = null;
        this.endLocationString = null;
        this.startTime = null;
        this.estimatedRideFare = 0.0f;

        Methods.showMessage("Information", "Ride fare saved", "");

        return "/driverRides/listDriverRides?faces-redirect=true";
    }

   //Gets Ride Fare Estimate

    public void getFareEstimate(String distance, String duration) {
        driver = (Driver) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("driver");

        Cab cab = driver.getCabId();

        // convert hours to mins if necessary
        float rideDuration;
        if (duration.contains("day")) {
            rideDuration = Float.parseFloat(duration.split(" day")[0].replace(",", "")) * 24 * 60;
        } else {
            if (duration.contains("hour")) {
                rideDuration = Float.parseFloat(duration.split(" hour")[0].replace(",", "")) * 60;
                String minutes = duration.split(" hour")[1];
                minutes = minutes.split(" ")[1];
                rideDuration += Float.parseFloat(minutes.split(" min")[0]);
            } else {
                rideDuration = Float.parseFloat(duration.split(" ")[0].replace(",", ""));
            }
        }


        float rideFareEstimate = calculateRideFareEstimate(Constants.CabType.values()[cab.getType()],
                Float.parseFloat(distance.split(" ")[0].replace(",", "")), rideDuration);

        rideFareEstimate /= cab.getCapacity();

        this.setEstimatedRideFare(rideFareEstimate);
    }

   //Calculates Ride Fare Estimate

    public float calculateRideFareEstimate(Constants.CabType cabType, float distance, float duration) {

        float baseFare = 0;
        float minimumCharge = 0;
        float chargePerKM = 0;
        float chargePerMinute = 0;

        switch (cabType) {
            case Hatchback:
                baseFare = (float) 2.55;
                minimumCharge = (float) 8;
                chargePerKM = (float) 1.75;
                chargePerMinute = (float) 0.35;
                break;
            case Sedan:
                baseFare = (float) 3.85;
                minimumCharge = (float) 10.5;
                chargePerKM = (float) 2.85;
                chargePerMinute = (float) 0.5;
                break;
            case SUV:
                baseFare = (float) 14;
                minimumCharge = (float) 25;
                chargePerKM = (float) 4.5;
                chargePerMinute = (float) 0.8;
                break;
        }

        return baseFare + (chargePerMinute * duration) + (chargePerKM * distance) + minimumCharge;

    }

    // Get Formatted Date (Timestamp)

    public String getFormattedDate(Timestamp t){

        if(t==null) return "-";

        SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy hh.mm aa");
        Date dt = new Date(t.getTime());
        return simpleDate.format(dt);
    }



    // Get Formatted Date (Date)

    public String getFormattedDate(Date t){

        if(t==null) return "-";

        SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy hh.mm aa");
        return simpleDate.format(t);
    }

}
