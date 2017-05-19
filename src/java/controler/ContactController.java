package controler;

import bean.Comment;
import bean.Contact;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.ContactFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("contactController")
@SessionScoped
public class ContactController implements Serializable {

    @EJB
    private service.ContactFacade ejbFacade;
    private List<Contact> items = null;
    private List<Contact> itemsFound;
    private Contact selected;

    public void send() {
        ejbFacade.create(selected);
        JsfUtil.addSuccessMessage("merci pour votre commentaire!!!");
    }

    public void destroy(Contact contact) {
        ejbFacade.remove(contact);
    }

    public ContactController() {
    }

    public void delete(Contact item) {
        ejbFacade.remove(item);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Contact> getItemsFound() {
        if (itemsFound == null) {
            itemsFound = new ArrayList<>();
        }
        return itemsFound;
    }

    public void setItemsFound(List<Contact> itemsFound) {
        this.itemsFound = itemsFound;
    }

    public Contact getSelected() {
        if (selected == null) {
            selected = new Contact();
        }
        return selected;
    }

    public void setSelected(Contact selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ContactFacade getFacade() {
        return ejbFacade;
    }

    public Contact prepareCreate() {
        selected = new Contact();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setDateContact(new Date());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ContactCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ContactUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ContactDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Contact> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Contact getContact(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Contact> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Contact> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Contact.class)
    public static class ContactControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContactController controller = (ContactController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contactController");
            return controller.getContact(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Contact) {
                Contact o = (Contact) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Contact.class.getName()});
                return null;
            }
        }

    }

}
