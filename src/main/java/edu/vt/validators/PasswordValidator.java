/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

// Password Validator
@FacesValidator("passwordValidator")

public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // Type cast the inputted "value" to String type
        String enteredPassword = (String) value;

        if (enteredPassword == null || enteredPassword.isEmpty()) {
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }


        int pwdLength = enteredPassword.length();
        for (int i = 0; i < pwdLength; i++) {
            if (Character.isWhitespace(enteredPassword.charAt(i))) {

                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        "Password cannot contain a whitespace!", "Please enter a valid password!"));
            }
        }


        // Regular EXpression (regex) to validate the strength of enteredPassword
        String regex = "^(?=.{8,32})(?=.*[!@#$%^&*()])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";

        if (!enteredPassword.matches(regex)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Password Failed!", "The password must be minimum 8 and maximum 32 "
                    + "characters long, contain at least one special character above the numbers on the keyboard, "
                    + "contain at least one uppercase letter, "
                    + "contain at least one lowercase letter, "
                    + "and contain at least one digit 0 to 9."));
        }
    }

}
