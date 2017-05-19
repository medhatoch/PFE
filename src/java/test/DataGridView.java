package test;

import bean.Vehicule;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import service.VehiculeFacade;
 
@Named("dataGridView")
@SessionScoped
public class DataGridView implements Serializable {
     
    private List<Vehicule> cars;
     
    private Vehicule selectedCar;
     
    @EJB
    private VehiculeFacade service;
     
    @PostConstruct
    public void init() {
        cars = service.findAll();
    }
 
    public List<Vehicule> getCars() {
        return cars;
    }
 
    public void setService(VehiculeFacade service) {
        this.service = service;
    }
 
    public Vehicule getSelectedCar() {
        return selectedCar;
    }
 
    public void setSelectedCar(Vehicule selectedCar) {
        this.selectedCar = selectedCar;
    }
}