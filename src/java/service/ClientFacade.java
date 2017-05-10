/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import controler.util.HashageUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;
    
    public boolean isEmailAlreadyUsed(String email) {
        System.out.println("service: email" + email);
        String query = "SELECT c FROM Client c WHERE c.email='" + email + "'";
        List<Client> clients = em.createQuery(query).getResultList();
        System.out.println("service: email" + clients.toString());
        if (clients != null) {
            System.out.println("we are in 1");
           /* if (clients.size() > 1) {*/
                System.out.println("we are in 2");
                if (clients.get(0) != null) {
                    System.out.println("we are in 3");
                    if (clients.get(0).getEmail() != null) {
                        System.out.println("we are in 4");
                        if (!clients.get(0).getEmail().isEmpty()) {
                            System.out.println("we are in 5");
                            return true;
                        }
                }
            }
        }
        return false;
    }

     public int createClient(Client client){
        Client loaded=find(client.getEmail());
        if(loaded!=null){
            return -1;
        }else if(client.getEmail().equals("")){
            return -2;
        }else if(client.getPassword().equals("")){
            return -3;
        }
        client.setPassword(HashageUtil.sha256(client.getPassword()));
        create(client);
        return 1;
    }

  public int seConnecter(Client client){
      Client loaded=find(client.getEmail());
      if(loaded==null){
          return -1;
      }
      if(loaded!=null){
          if(!loaded.getPassword().equals(HashageUtil.sha256(client.getPassword()))){
              return -2;
          }else{
              return 1;
          }
      }
      return 0;
  }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }
    
}
