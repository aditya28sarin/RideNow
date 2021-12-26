package edu.vt.controllers;

import edu.vt.EntityBeans.*;
import edu.vt.FacadeBeans.*;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

// Location Controller
@Named("locationController")
@SessionScoped
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class LocationController implements Serializable {

    //Instance Variables (Properties)

    private Driver driver;
    private List<Rider> riders;
    private Cab cab;
    private Date startTime;
    private Location startLocation;
    private Location endLocation;

    private Ride selected;

    private Rider rider;
    private String startLocationString;
    private String endLocationString;
    private List<Driver> listOfDrivers = null;

    private List<Rider> listOfRiders = null;

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

    //Getter and Setter Methods

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

    public void setSelected(Ride selected) {
        this.selected = selected;
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

    //Create a Ride

    public String createRide() {

        Methods.preserveMessages();

        try {
            // Instantiate a new Ride object
            Ride newRide = new Ride();

            newRide.setDriverID(driver);
            newRide.setStatus(RideStatus.NotStarted.ordinal());
            riders = new ArrayList<Rider>();

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

            rideFacade.create(newRide);
            riders.add(rider);
            // newRide.setRiders(riders);

            // create entry in rider - ride mapping table
            RideRider rideRider = new RideRider();
            rideRider.setRideId(newRide);
            rideRider.setRiderId(rider);

            rideRiderFacade.create(rideRider);

        } catch (EJBException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating ride!",
                    "See: " + ex.getMessage());
            return "";
        }

        Methods.showMessage("Information", "Ride Created",
                "");

        return "/index?faces-redirect=true";
    }


    // Instance Methods

    public List<Driver> getAllDrivers() {
        if (listOfDrivers == null) {
            listOfDrivers = driverFacade.findAll();
        }


        return listOfDrivers;
    }


    // Get all Driver Names

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

    // get All Riders
    public List<Rider> getAllRiders() {
        if (listOfRiders == null) {
            listOfRiders = riderFacade.findAll();
        }

        return listOfRiders;
    }

    // Get All Rider Names

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

    // enum for Ride Status
    public enum RideStatus {
        NotStarted,
        InProgress,
        Completed,
        Cancelled
    }

}
