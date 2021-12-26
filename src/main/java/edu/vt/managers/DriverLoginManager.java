
package edu.vt.managers;

import edu.vt.EntityBeans.Driver;
import edu.vt.FacadeBeans.DriverFacade;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

// DriverLoginManager

@Named("driverLoginManager")
@SessionScoped
public class DriverLoginManager implements Serializable {

    // Instance Variables (Properties)
    private String email;
    private String password;


    @EJB
    private DriverFacade driverFacade;

    // Getter and Setter Methods

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

    // Instance Methods


    // Sign in the User if the Entered Username and Password
    // are Valid and Redirect to Show the Profile Page

    public String loginUser() {

        // Since we will redirect to show the Profile page, invoke preserveMessages()
        Methods.preserveMessages();

        String enteredEmail = email;

        // Obtain the object reference of the User object from the entered username
        Driver driver = driverFacade.findByEmail(email);

        if (driver == null) {
            Methods.showMessage("Fatal Error", "Unknown Email!",
                    "Entered email " + enteredEmail + " does not exist!");
            return "";

        } else {


            String codedPassword = driver.getPassword();

            // Call the getter method to get the password entered by the user
            String enteredPassword = getPassword();


            try {
                if (!Password.verifyPassword(enteredPassword, codedPassword)) {
                    Methods.showMessage("Fatal Error", "Invalid Password!",
                            "Please Enter a Valid Password!");
                    return "";
                }
            } catch (Password.CannotPerformOperationException | Password.InvalidHashException ex) {
                Methods.showMessage("Fatal Error",
                        "Password Manager was unable to perform its operation!",
                        "See: " + ex.getMessage());
                return "";
            }

            // Verification Successful: Entered password = User's actual password

            // Initialize the session map with user properties of interest in the method below
            initializeSessionMap(driver);

            Methods.showMessage("Information", "Hello " + driver.getName() + "!", "You have logged in!");

            // Redirect to show the Profile page
            return "/driverAccount/profile?faces-redirect=true";
        }
    }

    // Initialize the Session Map to Hold Session Attributes of Interests

    public void initializeSessionMap(Driver driver) {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        // Store the object reference of the signed-in user
        sessionMap.put("driver", driver);

        // Store the First Name of the signed-in user
        sessionMap.put("name", driver.getName());

        // Store the Last Name of the signed-in user
        //sessionMap.put("last_name", user.getLastName());

        // Store the Username of the signed-in user
        //sessionMap.put("username", username);

        // Store signed-in user's Primary Key in the database
        sessionMap.put("driver_id", driver.getId());
    }

}
