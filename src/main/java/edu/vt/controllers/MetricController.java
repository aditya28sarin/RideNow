package edu.vt.controllers;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Ride;
import edu.vt.FacadeBeans.CabFacade;
import edu.vt.FacadeBeans.DriverFacade;
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


// Metrics Controller

@Named("metricController")
@SessionScoped

public class MetricController implements Serializable{
    @EJB
    private DriverFacade driverFacade;

    @EJB
    private RideFacade rideFacade;

    private int metricType = 0;
    List<Driver> listOfTopDrivers;
    List<Ride> listOfTopLongestRides;
    List<Ride> listOfTopShortestRides;
    private String selectedRowNumber = "0";

    // Getter and Setter Functions

    public int getMetricType() {
        return metricType;
    }

    public void setMetricType(int metricType) {
        this.metricType = metricType;
    }

    public String setMetricTypeAndGetData(int metricType) {
        this.metricType = metricType;
        return "/appMetrics/metrics?faces-redirect=true";
    }

    // Get List of Top Drivers

    public List<Driver> getListOfTopDrivers() {
        listOfTopDrivers = driverFacade.getTopRatedDrivers();
        return listOfTopDrivers;
    }

    public void setListOfTopDrivers(List<Driver> listOfTopDrivers) {
        this.listOfTopDrivers = listOfTopDrivers;
    }

    public List<Ride> getListOfTopLongestRides() {
        listOfTopLongestRides = rideFacade.getLongestRides();
        return listOfTopLongestRides;
    }

    public void setListOfTopLongestRides(List<Ride> listOfTopLongestRides) {
        this.listOfTopLongestRides = listOfTopLongestRides;
    }

    public List<Ride> getListOfTopShortestRides() {
        listOfTopShortestRides = rideFacade.getShortestRides();
        return listOfTopShortestRides;
    }

    public void setListOfTopShortestRides(List<Ride> listOfTopShortestRides) {
        this.listOfTopShortestRides = listOfTopShortestRides;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
    }

    // Reset Selected Items

    public void reset(){
        listOfTopLongestRides = null;
        listOfTopShortestRides = null;
        listOfTopDrivers = null;
    }
}
