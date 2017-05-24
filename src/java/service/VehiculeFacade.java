/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Vehicule;
import controler.util.SearchUtil;
import java.util.ArrayList;
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

    public List<Vehicule> disponibleVehicules() {
        List<Vehicule> list= em.createQuery("SELECT v FROM Vehicule v WHERE v.etat=" + 1).getResultList();
        if (list==null) {
            return new ArrayList<>();
        }
        return list;
    }

    public Vehicule findByMatr(String matr){
        Vehicule vehicule=(Vehicule) em.createQuery("SELECT m FROM Vehicule m WHERE m.matricule='"+matr+"'").getSingleResult();
        if (vehicule==null) {
            return new Vehicule();
        }
        System.out.println(vehicule);
        return vehicule;
    }
    public List<Vehicule> findMaxMin(Double prixMin, Double prixMax) {
        String rq = "SELECT v FROM Vehicule v WHERE 1=1";
        rq += SearchUtil.addConstraintMinMax("v", "prixParJour", prixMin, prixMax);
        List<Vehicule> list = em.createQuery(rq).getResultList();
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
    
    public List<Vehicule> findVehicule(Boolean etat,int nombrePlace,String modele,String categorie,String carburant,String marque,Double prixMax,Double prixMin){
        String requette = "SELECT v FROM Vehicule v WHERE 1=1";
        requette += SearchUtil.addConstraint("v", "etat", "=", etat);
        requette += SearchUtil.addConstraint("v", "nombrePlace", "=", nombrePlace);
        requette += SearchUtil.addConstraint("v", "modele", "=", modele);
        requette += SearchUtil.addConstraint("v", "categorie", "=", categorie);
        requette += SearchUtil.addConstraint("v", "carburant", "=", carburant);
        requette += SearchUtil.addConstraint("v", "marque", "=", marque);
        requette += SearchUtil.addConstraintMinMax("v", "prixParJour", prixMin, prixMax);
        return em.createQuery(requette).getResultList();
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VehiculeFacade() {
        super(Vehicule.class);
    }

}
