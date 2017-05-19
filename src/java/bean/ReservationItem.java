/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ayoub
 */
@Entity
public class ReservationItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Vehicule vehicule;
    private Double prixReservation=0D;
    private int nbrJours;
    @ManyToOne
    private Reservation reservation;
    @OneToOne
    private EtatLieu etatLieu;

    public EtatLieu getEtatLieu() {
        return etatLieu;
    }

    public void setEtatLieu(EtatLieu etatLieu) {
        this.etatLieu = etatLieu;
    }
    

    public int getNbrJours() {
        return nbrJours;
    }

    public void setNbrJours(int nbrJours) {
        this.nbrJours = nbrJours;
    }

    
    public Vehicule getVehicule() {
        if (vehicule==null) {
            vehicule=new Vehicule();
        }
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

    public Reservation getReservation() {
        if(reservation==null){
            reservation=new Reservation();
        }
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
        if (!(object instanceof ReservationItem)) {
            return false;
        }
        ReservationItem other = (ReservationItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.ReservationItem[ id=" + id + " ]";
    }
    
}
