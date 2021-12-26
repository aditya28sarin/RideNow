package edu.vt.controllers;

import edu.vt.EntityBeans.Cab;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.CabFacade;
import edu.vt.globals.Methods;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

//This is Cab Controller
@Named("cabController")
@SessionScoped
public class CabController implements Serializable {

//    Instance Variables

    private Cab selected;
    private String make;
    private String model;
    private String makeCheck = "";
    private List<String> listOfMakes;

    @EJB
    private CabFacade cabFacade;

//   Getter and Setter Methods
    public Cab getSelected() {
        return selected;
    }

    public void setSelected(Cab selected) {
        this.selected = selected;
    }




    // Prepare to create a new cab
    public void prepareCreate() {
        selected = new Cab();
    }

    // Create a new cab in the database
    public void create() {
        Methods.preserveMessages();

        persist(PersistAction.CREATE, "Cab Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;        // Remove selection
        }
    }

    // Update Selected Cab in Database
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE, "Cab Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
        }
    }

   // Deleted Selected Cab in Database
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE, "Cab Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
        }
    }

   // Perform CRUD operations in Database

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    cabFacade.edit(selected);
                } else {
                    cabFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
            }
        }
    }

    // Returns the Car Make from external API
    public List<String> getMakes() throws IOException, InterruptedException {
        List<String> makes = new ArrayList<String>();
        makes = cabFacade.getMakesFromAPI();
        make = makes.get(0);
        return makes;
    }

}
