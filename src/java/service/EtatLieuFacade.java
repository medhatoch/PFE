/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.EtatLieu;
import bean.EtatLieuItem;
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public void createEtatLieu(EtatLieu etatLieu){
        Double prixEtatLieu=0D;
        List<EtatLieuItem> list=etatLieuItemFacade.findItems(etatLieu);
        for (int i = 0; i < list.size(); i++) {
            EtatLieuItem item = list.get(i);
            prixEtatLieu+=item.getGarantit();
        }
        etatLieu.setPrix(prixEtatLieu);
        edit(etatLieu);
    } 

    public EtatLieuFacade() {
        super(EtatLieu.class);
    }
    
}
