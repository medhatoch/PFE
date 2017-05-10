/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.validator;

import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author lenovo
 */
@Named @ConversationScoped
public class PasswordValidator implements Serializable{
    
    public void passwordValidator(FacesContext context, UIComponent toValidate, Object value) {

    UIInput passwordField = (UIInput) context.getViewRoot().findComponent("registerForm:password");
    if (passwordField == null)
        throw new IllegalArgumentException(String.format("Unable to find component."));
    String password = (String) passwordField.getValue();
    String confirmPassword = (String) value;
    if (!confirmPassword.equals(password)) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
        throw new ValidatorException(message);
    }
}
    
}
