package controler;

import bean.Client;
import bean.Location;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.LocationFacade;

import java.io.Serializable;
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

@Named("locationController")
@SessionScoped
public class LocationController implements Serializable {

    @EJB
    private service.LocationFacade ejbFacade;
    @EJB
    private service.ClientFacade clientFacade;
    @EJB
    private service.VehiculeFacade vehiculeFacade;
    private String email;
    private String matr;
    private List<Location> items = null;
    private Location selected;
    
    public void rechercheParEmail(){
        selected.setClient(clientFacade.findByEmail(email));
    }
    public void rechercheVehicule(){
        selected.setVehicule(vehiculeFacade.findByMatr(matr));
    }

    public void calculatePrice(){
        selected.setPrixReservation(selected.getNbrJours()*selected.getVehicule().getPrixParJour());
    }
    public LocationController() {
    }

    public String getMatr() {
        if (matr==null) {
            matr="";
        }
        return matr;
    }

    public void setMatr(String matr) {
        this.matr = matr;
    }

    
    public Location getSelected() {
        if (selected==null) {
            selected=new Location();
        }
        return selected;
    }

    public String getEmail() {
        if (email==null) {
            email="";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    public void setSelected(Location selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LocationFacade getFacade() {
        return ejbFacade;
    }

    public Location prepareCreate() {
        selected = new Location();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.getVehicule().setEtat(false);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LocationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LocationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LocationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Location> getItems() {
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

    public Location getLocation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Location> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Location> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Location.class)
    public static class LocationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LocationController controller = (LocationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "locationController");
            return controller.getLocation(getKey(value));
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
            if (object instanceof Location) {
                Location o = (Location) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Location.class.getName()});
                return null;
            }
        }

    }

}
