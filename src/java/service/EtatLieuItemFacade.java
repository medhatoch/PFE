/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.EtatLieu;
import bean.EtatLieuItem;
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
public class EtatLieuItemFacade extends AbstractFacade<EtatLieuItem> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EtatLieuItem> findItems(EtatLieu etatLieu){
       List<EtatLieuItem> list=em.createQuery("SELECT e FROM EtatLieuItem e WHERE e.etatLieu.id='"+etatLieu.getId()+"'").getResultList();
        if (list==null) {
            return new ArrayList<>();
        }
        System.out.println("++++++++++Etat Lieu+++++++++");
        System.out.println(list);
        return list;
    }
    public EtatLieuItemFacade() {
        super(EtatLieuItem.class);
    }
    
}
