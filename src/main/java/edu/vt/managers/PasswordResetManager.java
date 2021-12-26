package edu.vt.managers;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Rider;
import edu.vt.FacadeBeans.DriverFacade;
import edu.vt.FacadeBeans.RiderFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;


// PasswordResetManager
@Named("passwordResetManager")
@SessionScoped
public class PasswordResetManager implements Serializable {


    // Instance Variables (Properties)

    private String email;
    private String password;
    private String confirmPassword;
    private String answerToSecurityQuestion;


    @EJB
    private DriverFacade driverFacade;

    @EJB
    private RiderFacade riderFacade;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void setAnswerToSecurityQuestion(String answerToSecurityQuestion) {
        this.answerToSecurityQuestion = answerToSecurityQuestion;
    }


    // Process the Email Submitted for Password Reset

    public String emailSubmit() {

        // Since we will redirect to show the SecurityQuestion page, invoke preserveMessages()
        Methods.preserveMessages();

        Driver driver = driverFacade.findByEmail(email);

        Rider rider = riderFacade.findByEmail(email);

        if (driver == null) {
            if (rider == null) {
                Methods.showMessage("Fatal Error", "Unknown Email Id!",
                        "Entered Email Id " + email + " does not exist!");
                return "";
            }
        }

        // Redirect to show the SecurityQuestion page
        return "/userPasswordChange/SecurityQuestion?faces-redirect=true";


    }

    // Return the Security Question Selected by
    // the User at the Time of Account Creation

    public String getSelectedSecurityQuestionForUsername() {

        // Obtain the object reference of the User object with username
        Driver driver = driverFacade.findByEmail(email);
        if (driver != null) {
            // Obtain the number of the security question selected by the user
            int questionNumber = driver.getSecurityQuestionNumber();

            // Return the security question corresponding to the question number
            return Constants.QUESTIONS[questionNumber];
        }

        Rider rider = riderFacade.findByEmail(email);

        // Obtain the number of the security question selected by the user
        int questionNumber = rider.getSecurityQuestionNumber();

        // Return the security question corresponding to the question number
        return Constants.QUESTIONS[questionNumber];
    }

    // Process the Submitted Answer to the Security Question

    public String securityAnswerSubmit() {

        // Since we will redirect to show the ResetPassword page, invoke preserveMessages()
        Methods.preserveMessages();

        String actualSecurityAnswer;
        String enteredSecurityAnswer;
        // Obtain the object reference of the User object with username
        Driver driver = driverFacade.findByEmail(email);

        if (driver != null) {
            actualSecurityAnswer = driver.getSecurityAnswer();
            enteredSecurityAnswer = getAnswerToSecurityQuestion();
        } else {
            Rider rider = riderFacade.findByEmail(email);

            actualSecurityAnswer = rider.getSecurityAnswer();
            enteredSecurityAnswer = getAnswerToSecurityQuestion();
        }

        if (actualSecurityAnswer.equals(enteredSecurityAnswer)) {
            /*
            Answer to the security question is correct. Redirect to show the ResetPassword page.
            */
            return "/userPasswordChange/ResetPassword?faces-redirect=true";

        } else {
            Methods.showMessage("Error",
                    "Answer to the Security Question is Incorrect!", "");
            return "";
        }
    }

    // Reset Password and Redirect to Show the Home Page

    public String resetPassword() {

        if (!password.equals(confirmPassword)) {
            Methods.showMessage("Fatal Error", "Unmatched Passwords!",
                    "Password and Confirm Password must Match!");
            return "";
        }

        // Since we will redirect to show the home page, invoke preserveMessages()
        Methods.preserveMessages();

        // Obtain the object reference of the User object with username
        Driver driver = driverFacade.findByEmail(email);

        if (driver != null) {
            try {

                String parts = Password.createHash(password);

                // Reset User object's password
                driver.setPassword(parts);

                // Update the database
                driverFacade.edit(driver);

                // Initialize the instance variables
                email = password = confirmPassword = answerToSecurityQuestion = "";

                Methods.showMessage("Information", "Your Password has been Reset",
                        "");

                // Redirect to show the index (home) page
                return "/index?faces-redirect=true";

            } catch (EJBException | Password.CannotPerformOperationException ex) {
                Methods.showMessage("Fatal Error",
                        "Something went wrong while resetting your password!",
                        "See: " + ex.getMessage());
            }
        }
        // if rider
        try {

            Rider rider = riderFacade.findByEmail(email);

            String parts = Password.createHash(password);

            // Reset User object's password
            rider.setPassword(parts);

            // Update the database
            riderFacade.edit(rider);

            // Initialize the instance variables
            email = password = confirmPassword = answerToSecurityQuestion = "";

            Methods.showMessage("Information", "Your Password has been Reset!",
                    "");

            // Redirect to show the index (home) page
            return "/index?faces-redirect=true";

        } catch (EJBException | Password.CannotPerformOperationException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while resetting your password!",
                    "See: " + ex.getMessage());
        }
        return "";
    }

    public boolean isDriverLoggedIn() {
        Driver driver = (Driver) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("driver");

        return driver != null;
    }

}
