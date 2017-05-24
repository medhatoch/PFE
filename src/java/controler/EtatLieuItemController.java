package controler;

import bean.EtatLieu;
import bean.EtatLieuItem;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.EtatLieuItemFacade;

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
import service.EtatLieuFacade;

@Named("etatLieuItemController")
@SessionScoped
public class EtatLieuItemController implements Serializable {

    @EJB
    private service.EtatLieuItemFacade ejbFacade;
    @EJB
    private EtatLieuFacade etatLieuFacade;
    private List<EtatLieuItem> items = null;
    private List<EtatLieuItem> itemsAdded;
    private EtatLieuItem selected;

    public void addEtatLieuItem() {
        if (itemsAdded == null) {
            itemsAdded = new ArrayList<>();
        }
        itemsAdded.add(selected);
    }
    public void save(){
        etatLieuFacade.createEtatLieu(items);//le service de creation est dans etatLieuFacade
    }

    public EtatLieuItemController() {
    }

    public List<EtatLieuItem> getItemsAdded() {
        if (itemsAdded == null) {
            itemsAdded = new ArrayList<>();
        }
        return itemsAdded;
    }

    public void setItemsAdded(List<EtatLieuItem> itemsAdded) {
        this.itemsAdded = itemsAdded;
    }

    public EtatLieuItem getSelected() {
        if (selected == null) {
            selected = new EtatLieuItem();
        }
        return selected;
    }

    public void findItems(EtatLieu etatLieu) {
        items = ejbFacade.findItems(etatLieu);
    }

    public void destroy(EtatLieuItem etatLieuItem) {
        ejbFacade.remove(etatLieuItem);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void setSelected(EtatLieuItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EtatLieuItemFacade getFacade() {
        return ejbFacade;
    }

    public EtatLieuItem prepareCreate() {
        selected = new EtatLieuItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EtatLieuItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EtatLieuItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EtatLieuItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<EtatLieuItem> getItems() {
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

    public EtatLieuItem getEtatLieuItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<EtatLieuItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<EtatLieuItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = EtatLieuItem.class)
    public static class EtatLieuItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EtatLieuItemController controller = (EtatLieuItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "etatLieuItemController");
            return controller.getEtatLieuItem(getKey(value));
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
            if (object instanceof EtatLieuItem) {
                EtatLieuItem o = (EtatLieuItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EtatLieuItem.class.getName()});
                return null;
            }
        }

    }

}
