/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.EtatLieu;
import bean.EtatLieuItem;
import bean.Reservation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class EtatLieuFacade extends AbstractFacade<EtatLieu> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;
    @EJB
    private EtatLieuItemFacade etatLieuItemFacade; 
    public void createEtatLieu(List<EtatLieuItem> etatLieuItems){
        Double prix=0D;
        EtatLieu etatLieu=new EtatLieu();
        create(etatLieu);
        for (int i = 0; i < etatLieuItems.size(); i++) {
            EtatLieuItem item = etatLieuItems.get(i);
            item.setEtatLieu(etatLieu);
            prix+=item.getGarantit();
            etatLieuItemFacade.create(item);
        }
        etatLieu.setPrix(prix);
        edit(etatLieu);
    }

    public List<EtatLieuItem> findByReservation(Reservation reservation){
        EtatLieu etatLieu=(EtatLieu) em.createQuery("SELECT e FROM EtatLieu e WHERE e.reservation.id='"+reservation.getId()+"'").getSingleResult();
        List<EtatLieuItem> list=etatLieuItemFacade.findItems(etatLieu);
        return list;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
//    public void createEtatLieu(EtatLieu etatLieu){
//        Double prixEtatLieu=0D;
//        List<EtatLieuItem> list=etatLieuItemFacade.findItems(etatLieu);
//        if (list==null) {
//            list=new ArrayList<>();
//        }
//        for (int i = 0; i < list.size(); i++) {
//            EtatLieuItem item = list.get(i);
//            prixEtatLieu+=item.getGarantit();
//        }
//        etatLieu.setPrix(prixEtatLieu);
//        edit(etatLieu);
//    } 

    public EtatLieuFacade() {
        super(EtatLieu.class);
    }
    
}
