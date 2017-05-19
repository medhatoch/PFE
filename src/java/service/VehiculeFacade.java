/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Vehicule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class VehiculeFacade extends AbstractFacade<Vehicule> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;
    
    public List<Vehicule> disponibleVehicules(){
        return em.createQuery("SELECT v FROM Vehicule v WHERE v.etat="+0).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VehiculeFacade() {
        super(Vehicule.class);
    }
    
}
