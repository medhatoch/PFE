/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Manager;
import bean.Reservation;
import bean.ReservationItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;
    @EJB
    private ReservationItemFacade reservationItemFacade;

    public void save(List<ReservationItem> list, Manager manager, Client client) {
        Double prixTotal = 0D;
        Reservation reservation = new Reservation();

        reservation.setClient(client);
        reservation.setManager(manager);
        create(reservation);

        for (int i = 0; i < list.size(); i++) {
            ReservationItem item = list.get(i);
            item.setReservation(reservation);
            prixTotal += item.getPrixReservation();
            reservationItemFacade.create(item);
        }
        reservation.setPrixTotal(prixTotal);
        edit(reservation);
    }

    //*****************Chart Functions******************
    public BarChartModel initBarModel(Date dateMin, Date dateMax) {
        List<Reservation> reservations = findAll();
        List<Reservation> results = new ArrayList<>();

        for (int i = 0; i < reservations.size(); i++) {
            Reservation item = reservations.get(i);
            if (item.getDateReservation().after(dateMin) && item.getDateReservation().before(dateMax)) {
                results.add(item);
            }
        }

        BarChartModel model = new BarChartModel();

        ChartSeries prixTotal = new ChartSeries();

        prixTotal.setLabel("prixTotal");

        for (int i = 0; i < results.size(); i++) {
            Reservation item = results.get(i);
            System.out.println(item);
            prixTotal.set(item.getDateReservation(), item.getPrixTotal());
        }

        model.addSeries(prixTotal);
        return model;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

 
    public List<ReservationItem> findItems(Reservation reservation){
        return em.createQuery("SELECT r FROM ReservationItem r WHERE r.reservation.id='"+reservation.getId()+"'").getResultList();
    }
    public void delete(Reservation reservation){
        List<ReservationItem> list=findItems(reservation);
        for (int i = 0; i < list.size(); i++) {
            ReservationItem item = list.get(i);
            reservationItemFacade.remove(item);
        }
        remove(reservation);
    }
    public ReservationFacade() {
        super(Reservation.class);
    }

}
