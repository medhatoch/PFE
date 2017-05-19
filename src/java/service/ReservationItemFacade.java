/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Reservation;
import bean.ReservationItem;
import bean.Vehicule;
import controler.util.PdfUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ayoub
 */
@Stateless
public class ReservationItemFacade extends AbstractFacade<ReservationItem> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @EJB
    ClientFacade clientFacade;
    
    
    public void generatePdf(ReservationItem reservationItem) throws JRException, IOException {
        
        Vehicule vehicule = reservationItem.getVehicule();
        
        
        
        Client client = clientFacade.find(SessionUtil.getConnectedUtilisateur().getEmail());
        
        System.out.println(client);
        Map<String, Object> params = new HashMap();
        params.put("nom", client.getNom());
        params.put("prenom", client.getPrenom());
        params.put("tel", client.getTel());
        params.put("adresse", client.getAdresse());
        params.put("localite", client.getLocalite());
        params.put("pays", client.getPays());
        params.put("marque", vehicule.getMarque());
        params.put("matricule", vehicule.getMatricule());
        params.put("categorie", vehicule.getCategorie());
        params.put("modele", vehicule.getModele());
        
        
        System.out.println(params);
        
        PdfUtil.generatePdf(findAll(), params, "contratReservat", "/jasper/contratReservat.jasper");
    }

    public List<ReservationItem> findItems(Reservation reservation) {
        List<ReservationItem> list = em.createQuery("SELECT r FROM ReservationItem r WHERE r.reservation.id='" + reservation.getId() + "'").getResultList();
        if (list == null) {
            return new ArrayList<>();
        }
        System.out.println(list);
        return list;
    }

    public ReservationItemFacade() {
        super(ReservationItem.class);
    }

}
