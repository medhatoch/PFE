/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ayoub
 */
@Entity
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Vehicule vehicule;
    @OneToOne
    private Client client;
    private Double prixReservation=0D;
    private int nbrJours;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDepart;
    private Time heure;
    @OneToOne
    private EtatLieu etatLieu;
    @OneToOne
    private Agence agenceDepart;
    @OneToOne
    private Agence agenceArrive;

    public Client getClient() {
        if (client==null) {
            client=new Client();
        }
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    
    public EtatLieu getEtatLieu() {
        if (etatLieu==null) {
            etatLieu=new EtatLieu();
        }
        return etatLieu;
    }

    public void setEtatLieu(EtatLieu etatLieu) {
        this.etatLieu = etatLieu;
    }
    
    

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    public Agence getAgenceDepart() {
        if (agenceDepart==null) {
            agenceDepart=new Agence();
        }
        return agenceDepart;
    }

    public void setAgenceDepart(Agence agenceDepart) {
        this.agenceDepart = agenceDepart;
    }

    public Agence getAgenceArrive() {
         if (agenceArrive==null) {
            agenceArrive=new Agence();
        }
        return agenceArrive;
    }

    public void setAgenceArrive(Agence agenceArrive) {
        this.agenceArrive = agenceArrive;
    }
    
    

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Double getPrixReservation() {
        return prixReservation;
    }

    public void setPrixReservation(Double prixReservation) {
        this.prixReservation = prixReservation;
    }

    public int getNbrJours() {
        return nbrJours;
    }

    public void setNbrJours(int nbrJours) {
        this.nbrJours = nbrJours;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Location{" + "id=" + id + ", vehicule=" + vehicule + ", client=" + client + ", prixReservation=" + prixReservation + ", nbrJours=" + nbrJours + ", dateDepart=" + dateDepart + ", heure=" + heure + ", etatLieu=" + etatLieu + ", agenceDepart=" + agenceDepart + ", agenceArrive=" + agenceArrive + '}';
    }

}
