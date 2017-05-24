/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Reservation;
import controler.util.HashageUtil;
import controler.util.SearchUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

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

    public Client findByEmail(String email) {
        Client client = (Client) em.createQuery("SELECT c FROM Client c WHERE c.email='" + email + "'").getSingleResult();
        if (client == null) {
            return new Client();
        }
        System.out.println(client);
        return client;
    }

    public List<Client> search(Client client) {
        String rq = "SELECT c FROM Client c WHERE 1=1";
        rq += SearchUtil.addConstraint("c", "email", "=", client.getEmail());
        rq += SearchUtil.addConstraint("c", "nom", "=", client.getNom());
        rq += SearchUtil.addConstraint("c", "prenom", "=", client.getPrenom());
        rq += SearchUtil.addConstraint("c", "cin", "=", client.getCin());
        rq += SearchUtil.addConstraint("c", "rc", "=", client.getRc());
        rq += SearchUtil.addConstraint("c", "nature", "=", client.getNature());
        List<Client> list = em.createQuery(rq).getResultList();
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public int createClient(Client client) {
        Client loaded = find(client.getEmail());
        if (loaded != null) {
            return -1;
        } else if (client.getEmail().equals("")) {
            return -2;
        } else if (client.getPassword().equals("")) {
            return -3;
        }
        client.setPassword(HashageUtil.sha256(client.getPassword()));
        create(client);
        return 1;
    }

    public int seConnecter(Client client) {
        Client loaded = find(client.getEmail());
        if (loaded == null) {
            System.out.println("null+++++++");
            return -1;
        }
        if (!loaded.getPassword().equals(HashageUtil.sha256(client.getPassword()))) {
            return -2;
        } else {
            return 1;
        }
    }

    public BarChartModel initBarModelClient() {
        List<Client> clients = findAll();
        List<Client> results = new ArrayList<>();
//        int nbr = 0;
//        for (int i = 0; i < clients.size(); i++) {
//            Client item = clients.get(i);
//            if (item.getDateInscription().after(dateMin) && item.getDateInscription().before(dateMax)) {
//                results.add(item);
//                nbr++;
//            }
//        }

        BarChartModel model = new BarChartModel();

        ChartSeries nomreClientsTotal = new ChartSeries();
        ChartSeries nomreClients = new ChartSeries();

        nomreClients.setLabel("Nombre de clients");

        nomreClients.set("nombre de clients", count());
        nomreClientsTotal.set("nombre de clients total", count());

        model.addSeries(nomreClients);
        model.addSeries(nomreClientsTotal);
        return model;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }

}
