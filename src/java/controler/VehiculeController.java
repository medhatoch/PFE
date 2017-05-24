package controler;

import bean.Vehicule;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.VehiculeFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("vehiculeController")
@SessionScoped
public class VehiculeController implements Serializable {

    @EJB
    private service.VehiculeFacade ejbFacade;
    
    private List<Vehicule> items = null;
    private List<Vehicule> itemsFound=null;
    private Vehicule selected;
    private Double prixMax = 0D;
    private Double prixMin = 0D;
    private Boolean etat;
    private int nombrePlace;
    private String modele;
    private String categorie;
    private String carburant;
    private String marque;
    
    
    

    public void search() {
        itemsFound = ejbFacade.findMaxMin(prixMin, prixMax);
    }
    
    public void searchAdmin() {
        items = ejbFacade.findVehicule(etat, nombrePlace, modele, categorie, carburant, marque, prixMax, prixMin);
    }

    public VehiculeController() {
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public int getNombrePlace() {
        return nombrePlace;
    }

    public void setNombrePlace(int nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    
    public List<Vehicule> getItemsFound() {
        if (itemsFound==null) {
             return ejbFacade.findAll();
        }
       return itemsFound;
    }

    public void setItemsFound(List<Vehicule> itemsFound) {
        this.itemsFound = itemsFound;
    }

    public Double getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(Double prixMax) {
        this.prixMax = prixMax;
    }

    public Double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Vehicule getSelected() {
        if (selected == null) {
            selected = new Vehicule();
        }
        return selected;
    }

    public void setSelected(Vehicule selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VehiculeFacade getFacade() {
        return ejbFacade;
    }

    public Vehicule prepareCreate() {
        selected = new Vehicule();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setEtat(true);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VehiculeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VehiculeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VehiculeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Vehicule> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        if (getFacade().findAll()==null) {
            return new ArrayList<>();
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

    public Vehicule getVehicule(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Vehicule> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Vehicule> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Vehicule.class)
    public static class VehiculeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VehiculeController controller = (VehiculeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vehiculeController");
            return controller.getVehicule(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Vehicule) {
                Vehicule o = (Vehicule) object;
                return getStringKey(o.getMatricule());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Vehicule.class.getName()});
                return null;
            }
        }

    }

}
