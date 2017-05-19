
package controler;

import bean.Client;
import bean.Manager;
import bean.Reservation;
import bean.ReservationItem;
import bean.Vehicule;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import service.ReservationFacade;

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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import service.ReservationItemFacade;

@Named("reservationController")
@SessionScoped
public class ReservationController implements Serializable {

    @EJB
    private service.ReservationFacade ejbFacade;
    @EJB
    private service.ReservationItemFacade ejbItemsFacade;
    private boolean v1;
    private boolean v2;
    private boolean v3;
    private boolean v4;
    private boolean v5;
    private boolean v6;
    private boolean v7;
    private boolean v8;
    private boolean v9;
    private List<Reservation> items = null;
    private List<Reservation> itemsFound;
    private List<ReservationItem> itemsSelected = new ArrayList<>();
    private List<Vehicule> vehicules = new ArrayList<>();
    private Double prixMin;
    private Double prixMax;
    private Reservation selected;
    private Date dateRetour = new Date();
    private Date dateMin;
    private Date dateMax;
    private int nbrJours;

    public void save() {
        ejbFacade.save(itemsSelected, null, null);
        JsfUtil.addSuccessMessage("Validée");
    }

    public void reserver(Vehicule vehicule) {
        vehicule.setEtat(false);
        vehicules.add(vehicule);
        ReservationItem item = new ReservationItem();
        item.setVehicule(vehicule);
        item.setNbrJours(nbrJours);
        item.setPrixReservation(nbrJours * vehicule.getPrixParJour());
        itemsSelected.add(item);
        JsfUtil.addSuccessMessage("selected!!!");
    }

    public void destroy(Reservation reservation) {
        ejbFacade.delete(reservation);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    //**************************CHARTS*****************
    private BarChartModel barModel;

    public void init() {
        createBarModel();
    }

    public BarChartModel getBarModel() {
        if (barModel == null) {
            barModel = new BarChartModel();
        }
        return barModel;
    }

    public void createBarModel() {
        barModel = ejbFacade.initBarModel(dateMin, dateMax);

        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("prix");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Date");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

    //********************ENDCHARTS*******************
//    public void findItems() {
//        selected.setReservationItems(ejbItemsFacade.findByReservation(selected));
//    }
    public ReservationController() {
    }

    public Date getDateMin() {
        return dateMin;
    }

    public void setDateMin(Date dateMin) {
        this.dateMin = dateMin;
    }

    public Date getDateMax() {
        return dateMax;
    }

    public void setDateMax(Date dateMax) {
        this.dateMax = dateMax;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public List<ReservationItem> getItemsSelected() {
        if (itemsSelected == null) {
            itemsSelected = new ArrayList<>();
        }
        return itemsSelected;
    }

    public int getNbrJours() {
        return nbrJours;
    }

    public void setNbrJours(int nbrJours) {
        this.nbrJours = nbrJours;
    }

    public void setItemsSelected(List<ReservationItem> itemsSelected) {
        this.itemsSelected = itemsSelected;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public List<Reservation> getItemsFound() {
        if (itemsFound == null) {
            itemsFound = new ArrayList<>();
        }
        return itemsFound;
    }

    public void setItemsFound(List<Reservation> itemsFound) {
        this.itemsFound = itemsFound;
    }

    public Double getPrixMin() {
        if (prixMin == null) {
            prixMin = 0D;
        }
        return prixMin;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Double getPrixMax() {
        if (prixMax == null) {
            prixMax = 0D;
        }
        return prixMax;
    }

    public void setPrixMax(Double prixMax) {
        this.prixMax = prixMax;
    }

    public Reservation getSelected() {
        if (selected == null) {
            selected = new Reservation();
        }
        return selected;
    }

    public void setSelected(Reservation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ReservationFacade getFacade() {
        return ejbFacade;
    }

    public boolean isV1() {
        return v1;
    }

    public void setV1(boolean v1) {
        this.v1 = v1;
    }

    public boolean isV2() {
        return v2;
    }

    public void setV2(boolean v2) {
        this.v2 = v2;
    }

    public boolean isV3() {
        return v3;
    }

    public void setV3(boolean v3) {
        this.v3 = v3;
    }

    public boolean isV4() {
        return v4;
    }

    public void setV4(boolean v4) {
        this.v4 = v4;
    }

    public boolean isV5() {
        return v5;
    }

    public void setV5(boolean v5) {
        this.v5 = v5;
    }

    public boolean isV6() {
        return v6;
    }

    public void setV6(boolean v6) {
        this.v6 = v6;
    }

    public boolean isV7() {
        return v7;
    }

    public void setV7(boolean v7) {
        this.v7 = v7;
    }

    public boolean isV8() {
        return v8;
    }

    public void setV8(boolean v8) {
        this.v8 = v8;
    }

    public boolean isV9() {
        return v9;
    }

    ////************
    public void setV9(boolean v9) {
        this.v9 = v9;
    }

    ///*************
    public Reservation prepareCreate() {
        selected = new Reservation();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setDateReservation(new Date());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ReservationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ReservationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ReservationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Reservation> getItems() {
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

    public Reservation getReservation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Reservation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Reservation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Reservation.class)
    public static class ReservationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReservationController controller = (ReservationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reservationController");
            return controller.getReservation(getKey(value));
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
            if (object instanceof Reservation) {
                Reservation o = (Reservation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Reservation.class.getName()});
                return null;
            }
        }

    }

}
