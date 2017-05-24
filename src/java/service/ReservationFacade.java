/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.EtatLieu;
import bean.Manager;
import bean.Reservation;
import bean.ReservationItem;
import bean.Vehicule;
import controler.util.PdfUtil;
import controler.util.SearchUtil;
import controler.util.SessionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;
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
    @EJB
    private EtatLieuFacade etatLieuFacade;

    public void save(List<ReservationItem> list, Manager manager, Client client) {
        Double prixTotal = 0D;
        Reservation reservation = new Reservation();
        EtatLieu etatLieu = new EtatLieu();
        etatLieu.setReservation(reservation);
        reservation.setClient(client);
        reservation.setManager(manager);
        create(reservation);

        for (int i = 0; i < list.size(); i++) {
            ReservationItem item = list.get(i);
            item.setReservation(reservation);
            item.getVehicule().setEtat(false);
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

    public List<Reservation> search(Reservation reservation, Date min, Date max, Double prixMin, Double prixMax) {
        String rq = "SELECT r FROM Reservation r WHERE 1=1";
        rq += SearchUtil.addConstraint("r", "client.id", "=", reservation.getClient().getEmail());
        rq += SearchUtil.addConstraintMinMaxDate("r", "dateReservation", min, max);
        rq += SearchUtil.addConstraintMinMax("r", "prixTotal", prixMin, prixMax);
        List<Reservation> list = em.createQuery(rq).getResultList();
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public List<ReservationItem> findItems(Reservation reservation) {
        return em.createQuery("SELECT r FROM ReservationItem r WHERE r.reservation.id='" + reservation.getId() + "'").getResultList();
    }

    public void delete(Reservation reservation) {
        List<ReservationItem> list = findItems(reservation);
        for (int i = 0; i < list.size(); i++) {
            ReservationItem item = list.get(i);
            reservationItemFacade.remove(item);
        }
        remove(reservation);
    }
    
    public List<Reservation> findReservation(Client client,Manager manager,Date dateMin,Date dateMax,Double prixMax,Double prixMin){
        String requette = "SELECT r FROM Reservation r WHERE 1=1";
//        requette += SearchUtil.addConstraint("r", "client.email", "=", client.getEmail());
//        requette += SearchUtil.addConstraint("r", "manager.login", "=", manager.getLogin());
        requette += SearchUtil.addConstraintMinMaxDate("r", "dateReservation", dateMin, dateMax);
        requette += SearchUtil.addConstraintMinMax("r", "prixTotal", prixMin, prixMax);
        return em.createQuery(requette).getResultList();
    }

    public ReservationFacade() {
        super(Reservation.class);
    }

    
    public void generatePdf(Reservation reservation) throws JRException, IOException {
        List<ReservationItem> list=reservationItemFacade.findItems(reservation);
        Client client = SessionUtil.getConnectedClient();
        if (client != null) {
            System.out.println(client);
            Map<String, Object> params = new HashMap();
            params.put("nom", client.getNom());
            params.put("prenom", client.getPrenom());
            params.put("tel", client.getTel());
            params.put("adresse", client.getAdresse());
            params.put("localite", client.getLocalite());
            params.put("pays", client.getPays());
            params.put("prix",reservation.getPrixTotal());
            params.put("agent",reservation.getManager());
            System.out.println(params);
            PdfUtil.generatePdf(list, params, "contratReservat", "/jasper/contratReservat.jasper");
        }
    }
}
