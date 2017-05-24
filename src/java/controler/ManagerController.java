package controler;

import bean.Manager;
import controler.util.EmailUtil;
import controler.util.HashageUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.ManagerFacade;

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
import javax.mail.MessagingException;

@Named("managerController")
@SessionScoped
public class ManagerController implements Serializable {

    @EJB
    private service.ManagerFacade ejbFacade;
    private List<Manager> items = null;
    private Manager selected;
    private Manager connectedManager;

    public ManagerController() {
    }
    public String gotToProfile(Manager manager){
        selected=SessionUtil.getConnectedManager();
        return "/manager/Profile?faces-redirect=ture";
    }
    public String updateProfile(){
        ejbFacade.edit(selected);
        return "/location/List?faces-redirect=true";
    }
    public String deconnecter(){
        SessionUtil.unSetManager(SessionUtil.getConnectedManager());
        return "/manager/Login?faces-redirect=true";
    }

    public String seConnecte() {
        int res = ejbFacade.connect(selected.getLogin(),selected.getPassword());
        if (res == 1) {
            SessionUtil.registerManager(selected);
            System.out.println("1");
            System.out.println(selected);
            JsfUtil.addSuccessMessage("Vous etes connectée en tant que admin site!!!");
            return "/admin/ContactAdmin?faces-redirect=true";
        }
        System.out.println(res);
        return "/manager/Login?faces-redirect=true";
    }
      public String seConnecteApp() {
        int res = ejbFacade.seConnecter(selected);
        if (res == 1) {
            SessionUtil.registerManager(selected);
            JsfUtil.addSuccessMessage("Vous etes connectée en tant que application user!!!");
            return "/location/List?faces-redirect=true";
        }
        return "/manager/Login?faces-redirect=true";
    }

    public String forgotPassword() throws MessagingException {
        Manager manager = ejbFacade.find(selected);
        if (selected.getLogin().equals(manager.getLogin()) && selected.getEmail().equals(manager.getEmail())) {
            EmailUtil.sendMail("fromus", "passwrd", "msg", selected.getEmail(), "Reinitialisation du mot de pass");
            return "/manager/Login?faces-redirect=true";
        }
        return "/index?faces-redirect=true";
    }

    public Manager getConnectedManager() {
        if (connectedManager == null) {
            connectedManager = SessionUtil.getConnectedManager();
        }
        return connectedManager;
    }

    public void setConnectedManager(Manager connectedManager) {
        this.connectedManager = connectedManager;
    }

    public Manager getSelected() {
        if (selected == null) {
            selected = new Manager();
        }
        return selected;
    }

    public void setSelected(Manager selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ManagerFacade getFacade() {
        return ejbFacade;
    }

    public Manager prepareCreate() {
        selected = new Manager();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ManagerCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ManagerUpdated"));
    }

    public void destroy(Manager manager) {
        ejbFacade.remove(manager);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ManagerDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Manager> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                switch (persistAction) {
                    case CREATE:
                        getFacade().createManager(selected);
                        JsfUtil.addSuccessMessage(successMessage);
                        break;
                    case DELETE:
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage(successMessage);
                        break;
                    case UPDATE:
                        selected.setPassword(HashageUtil.sha256(selected.getPassword()));
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage("removed");
                        break;
                    default:
                        JsfUtil.addSuccessMessage("!!!!!!!");
                }
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

    public Manager getManager(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Manager> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Manager> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Manager.class)
    public static class ManagerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ManagerController controller = (ManagerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "managerController");
            return controller.getManager(getKey(value));
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
            if (object instanceof Manager) {
                Manager o = (Manager) object;
                return getStringKey(o.getLogin());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Manager.class.getName()});
                return null;
            }
        }

    }

}
