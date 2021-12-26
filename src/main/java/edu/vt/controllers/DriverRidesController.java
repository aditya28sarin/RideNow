package edu.vt.controllers;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Location;
import edu.vt.EntityBeans.Ride;
import edu.vt.EntityBeans.Rider;
import edu.vt.FacadeBeans.LocationFacade;
import edu.vt.FacadeBeans.RideFacade;
import edu.vt.FacadeBeans.RiderFacade;
import edu.vt.FacadeBeans.DriverFacade;
import edu.vt.FacadeBeans.RideRiderFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.IOException;
import java.io.Serializable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.mail.MessagingException;


// Driver Rides Controller
@Named("driverRidesController")
@SessionScoped

public class DriverRidesController implements Serializable {

    //Instance Variables
    @Inject
    private EmailController emailController;

    @Inject
    private TextMessageController textMessageController;

    @EJB
    private RideFacade rideFacade;

    @EJB
    private RiderFacade riderFacade;

    @EJB
    private RideRiderFacade rideRiderFacade;

    @EJB
    private LocationFacade locationFacade;

    @EJB
    private DriverFacade driverFacade;

    // 'selected' contains the object reference of the selected Ride object
    private Ride selected;

    private List<Ride> listOfRides = null;

    // Selected row number in p:dataTable in ListDriverRides.xhtml
    private String selectedRowNumber = "0";

    private Driver driver;

    private List<Rider> riders;

    private String staticMapImageUrl;

    private String startDestinationDistance;

    private String startDestinationEstimatedTime;

    private Date startTime;

    private String rideType;


   // Sends Email and Text Message to the Rider

    private boolean sendEmailAndTextMessage(Rider rider, String subject, String message) throws MessagingException {
        emailController.setEmailTo(rider.getEmail());
        emailController.setEmailSubject(subject);
        emailController.sendEmail("Hi " + rider.getName() + "! " + message);

        // Send Join Ride Confirmation text SMS to the Rider
        textMessageController.setCellPhoneNumber(rider.getPhoneNo());
        textMessageController.setCellPhoneCarrierDomain(rider.getCellPhoneCarrier());  // T-Mobile by default
        textMessageController.setMmsTextMessage("Hi " + rider.getName() + "! " + message);

        textMessageController.sendTextMessage();

        return true;
    }

   // Getter and Setter Methods

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Ride getSelected() {
        return selected;
    }

    public void setSelected(Ride selected) {
        this.selected = selected;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
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

    // Return the List of Rides that Belong to the Signed-In Driver

    public List<Ride> getListOfRides() {

        if (listOfRides == null) {

            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            Driver signedInDriver = (Driver) sessionMap.get("driver");

            // Obtain the database primary key of the signedInDriver object
            Integer primaryKey = signedInDriver.getId();

            // Obtain only those files from the database that belong to the signed-in user
            listOfRides = rideFacade.findRidesByDriverPrimaryKey(primaryKey);
        }
        return listOfRides;
    }

    // Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     UserFileFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    rideFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.

                     UserFileFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    rideFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);

            } catch (EJBException ex) {

                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
            }
        }
    }

    // Get Ride Function
    public Ride getRide(Integer id) {
        return rideFacade.find(id);
    }

    public void clearDriverHistory() {
        listOfRides = null;
        selected = null;
    }

   // Delete Selected Ride
    public String deleteSelectedRide() {

        Ride rideToDelete = selected;
        /*
        We need to preserve the messages since we will redirect to show a
        different JSF page after successful deletion of the user file.
         */
        Methods.preserveMessages();

        if (rideToDelete == null) {
            Methods.showMessage("Fatal Error", "No Ride Selected!", "You do not have a ride to delete!");
            return "";
        } else {

            try {
                //rideFacade.remove(rideToDelete);
                rideRiderFacade.deleteSelectedRide(selected);
                rideFacade.deleteSelectedRide(selected);


                Methods.showMessage("Information", "Ride Delete", "");

                listOfRides = null;

                return "/driverRides/listDriverRides?faces-redirect=true";
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return "";
    }


    // Start Selected Ride
    public String startSelectedRide() {

        Ride rideToStart = selected;

        Methods.preserveMessages();

        if (rideToStart == null) {
            Methods.showMessage("Fatal Error", "No Ride Selected!", "You do not have a ride to start!");
            return "";
        } else {

            try {

                rideToStart.setStatus(Constants.RideStatus.InProgress.ordinal());
                rideFacade.edit(rideToStart);

                List<Rider> riders = rideRiderFacade.getRiders(selected.getId());

                //set sum of earnings for a ride
                float sumTripEarnings = (riders.size()) * selected.getFare();
                selected.setTripEarning(sumTripEarnings);
                rideFacade.edit(selected);

                // Set Driver Trips
                Driver driver = rideToStart.getDriverID();
                int numOfTrips = driver.getNumberOfTrips();
                numOfTrips++;
                driver.setNumberOfTrips(numOfTrips);

                float totalDist = rideToStart.getDistance() + driver.getTotalKmsDriven();
                driver.setTotalKmsDriven(totalDist);

                //Set driver total earnings
                float driverTotalEarnings = driver.getTotalEarnings();
                driverTotalEarnings += sumTripEarnings;
                driver.setTotalEarnings(driverTotalEarnings);
                driverFacade.edit(driver);

                for (int i = 0; i < riders.size(); i++) {
                    //set rider trip
                    int trips = riders.get(i).getNumberOfTrips();
                    trips++;
                    riders.get(i).setNumberOfTrips(trips);

                    //set rider total expenditure
                    float totalExpense = riders.get(i).getTotalSpend() + selected.getFare();
                    riders.get(i).setTotalSpend(totalExpense);

                    riderFacade.edit(riders.get(i));
                }

                Methods.showMessage("Information", "Ride Started", "");

                return "/driverRides/listDriverRides?faces-redirect=true";
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return "";
    }

    // End Selected Ride
    public String endSelectedRide() {

        Ride rideToEnd = selected;

        Methods.preserveMessages();

        if (rideToEnd == null) {
            Methods.showMessage("Fatal Error", "No Ride Selected!", "You do not have a ride to end!");
            return "";
        } else {

            try {

                rideToEnd.setTripEndTime(new Timestamp(System.currentTimeMillis()));
                rideToEnd.setStatus(Constants.RideStatus.Completed.ordinal());
                rideFacade.edit(rideToEnd);

                // Send End Ride Confirmation E-Mail and Text SMS to the all the Riders in the Ride
                List<Rider> riders = rideRiderFacade.getRiders(rideToEnd.getId());

                String endRideMessage = "You have successfully ended the ride from " + selected.getStartLocation().getLandmarkName() +
                        " to " + selected.getDestinationLocation().getLandmarkName() +
                        ". Hope you enjoyed the ride! Please rate your Driver";

                for (Rider rider : riders) {
                    // Send End Ride Confirmation E-Mail and Text SMS to the Rider
                    sendEmailAndTextMessage(rider, "Ride Ended!", endRideMessage);
                }

                Methods.showMessage("Information", "Ride Ended", "");

                return "/driverRides/listDriverRides?faces-redirect=true";
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return "";
    }


    // Get Ride Status
    public String getRideStatus(int rideStatusValue) {
        return Constants.RideStatus.values()[rideStatusValue].name();
    }


    // Create Ride
    public String createRide() {

        Methods.preserveMessages();

        try {
            // Instantiate a new Ride object
            Ride newRide = selected;

            newRide.setTripStartTime(new Timestamp(selected.getStartTime(1).getTime()));

            locationFacade.edit(newRide.getStartLocation());
            locationFacade.edit(newRide.getDestinationLocation());
            rideFacade.edit(newRide);

            // Google static map api
            String imageUrl = locationFacade.generateStaticMapImageUrl(selected.getStartLocation().getLandmarkName(), selected.getDestinationLocation().getLandmarkName());
            setStaticMapImageUrl(imageUrl);

            // Google Distance matrix api
            ArrayList<String> distance = locationFacade.calculateDistance(selected.getStartLocation().getLandmarkName(), selected.getDestinationLocation().getLandmarkName());
            setStartDestinationDistance(distance.get(0));
            setStartDestinationEstimatedTime(distance.get(1));

        } catch (EJBException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating ride!",
                    "See: " + ex.getMessage());
            return "";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Methods.preserveMessages();

        Methods.showMessage("Information", "Ride Updated",
                "");

        return "/ride/viewEdited?faces-redirect=true";
    }

    // Get all rider names current ride
    public List<String> getAllRiderNamesInRide() {
        List<String> riderNames = new ArrayList<>();

        if (riders != null) {
            for (Rider rider : riders) {
                riderNames.add(rider.getName());
            }
        }

        return riderNames;
    }


    // Get Riders from ride ID
    public String getRiders(int RideId) {
        List<Rider> riders;
        riders = rideRiderFacade.getRiders(RideId);

        List<String> riderNames = new ArrayList<String>();
        for (Rider rider : riders) {
            String name = rider.getName();
            riderNames.add(name);
        }

        if(riderNames.size()==0)
        {
            return "-";
        }
        else
        {
            String ridersNames = "";
            for(int i=0;i<riderNames.size();i++){
                if(i!=0){
                    ridersNames += ", ";
                }
                ridersNames += riderNames.get(i);
            }
            return ridersNames;
        }
    }

}


