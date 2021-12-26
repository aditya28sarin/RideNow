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

// Email Validator
@FacesValidator("emailValidator")

public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // Type cast the inputted "value" to String type
        String email = (String) value;

        if (email == null || email.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }


        // Regular EXpression (regex) to validate the email address entered
        String regex = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!email.matches(regex)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Unrecognized Email Address!", "Please Enter a Valid Email Address!"));
        }
    }

}
