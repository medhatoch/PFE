package controler;

import bean.Client;
import com.sun.javafx.scene.control.SelectedCellsMap;
import controler.util.EmailUtil;
import controler.util.HashageUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.ClientFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.MessagingException;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;

@Named("clientController")
@SessionScoped
public class ClientController implements Serializable {

    @EJB
    private service.ClientFacade ejbFacade;
    private List<Client> items = null;
    private List<Client> itemsFound;
    private Client selected;
    private Client selected1;
    private String email;
    private Client connectedClient;
    private Date date = new Date();
    private Date dateMin;
    private Date dateMax;

    public String seConnecte() {
        int res = ejbFacade.seConnecter(selected);
        if (res == 1) {
            SessionUtil.registerClient(selected);
            System.out.println("1");
            JsfUtil.addSuccessMessage("Vous etes connectée!!");
            return "/client/Reserver?faces-redirect=true";
        }
        System.out.println("null+++++>controller");
        JsfUtil.addErrorMessage("Mot de pass ou login incorrect!!");
        return "/client/Login?faces-redirect=true";
    }

    public String forgotPassword() throws MessagingException {
        Client client = ejbFacade.find(selected);
        if (selected.getEmail() == client.getEmail()) {
            //mailUtil.sendMail("ourmail","*****","message",selected.getEmail(),"resetpassword oblet");
            return "/client/Login?faces-redirect=true";
        }
        return "/index?faces-redirect=true";
    }

    public void search() {
        items = ejbFacade.search(selected);
    }
    public void rechercherParEmail(){
        selected1=ejbFacade.findByEmail(email);
    }
    ///------------Charts-----------
    private BarChartModel barModel;

    public void init() {
        barModel = createBarModel();
    }

    public BarChartModel getBarModel() {
        if (barModel == null) {
            barModel = new BarChartModel();
        }
        return barModel;
    }

    public BarChartModel createBarModel() {
        barModel = ejbFacade.initBarModelClient();

        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Durée");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Nombre");
        yAxis.setMin(0);
        yAxis.setMax(200);
        return barModel;
    }

    public ClientController() {
    }

    public Client getConnectedClient() {
        if (connectedClient == null) {
            connectedClient = SessionUtil.getConnectedClient();
        }
        return connectedClient;
    }

    public Client getSelected1() {
        if (selected1==null) {
           selected1=new Client();
        }
        return selected1;
    }

    public void setSelected1(Client selected1) {
        this.selected1 = selected1;
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

    
    public void setConnectedClient(Client connectedClient) {
        this.connectedClient = connectedClient;
    }

    public List<Client> getItemsFound() {
        if (itemsFound == null) {
            return ejbFacade.findAll();
        }
        return itemsFound;
    }

    public void setItemsFound(List<Client> itemsFound) {
        this.itemsFound = itemsFound;
    }

    public Date getDateMin() {
        if (dateMin == null) {
            dateMin = new Date();
        }
        return dateMin;
    }

    public void setDateMin(Date dateMin) {
        this.dateMin = dateMin;
    }

    public Date getDateMax() {
        if (dateMax == null) {
            dateMax = new Date();
        }
        return dateMax;
    }
    

    public void setDateMax(Date dateMax) {
        this.dateMax = dateMax;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Client getSelected() {
        if (selected == null) {
            selected = new Client();
        }
        return selected;
    }

    public void setSelected(Client selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClientFacade getFacade() {
        return ejbFacade;
    }

    public Client prepareCreate() {
        selected = new Client();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
        selected.setDateInscription(new Date());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClientCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return "/index?faces-redirect=true";
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClientUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClientDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Client> getItems() {
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
                        getFacade().createClient(selected);
                        JsfUtil.addSuccessMessage("creted");
                        break;
                    case DELETE:
                        getFacade().remove(selected);
                        JsfUtil.addSuccessMessage("deleted");
                        break;
                    case UPDATE:
                        selected.setPassword(HashageUtil.sha256(selected.getPassword()));
                        getFacade().edit(selected);
                        JsfUtil.addSuccessMessage("modified");
                        break;
                    default:
                        JsfUtil.addSuccessMessage("!!!!!!");
                        break;
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

    public Client getClient(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Client> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Client> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Client.class)
    public static class ClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClientController controller = (ClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clientController");
            return controller.getClient(getKey(value));
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
            if (object instanceof Client) {
                Client o = (Client) object;
                return getStringKey(o.getEmail());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Client.class.getName()});
                return null;
            }
        }

    }

}
