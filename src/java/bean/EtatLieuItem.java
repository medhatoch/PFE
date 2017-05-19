/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author ayoub
 */
@Entity
public class EtatLieuItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private ComposantVoiture composantVoiture;
    private Double garantit=0D;
    @ManyToOne
    private EtatLieu etatLieu;

    public ComposantVoiture getComposantVoiture() {
        if(composantVoiture==null){
            composantVoiture=new ComposantVoiture();
        }
        return composantVoiture;
    }

    public void setComposantVoiture(ComposantVoiture composantVoiture) {
        this.composantVoiture = composantVoiture;
    }
    
    

    public Double getGarantit() {
        return garantit;
    }

    public void setGarantit(Double garantit) {
        this.garantit = garantit;
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
        if (!(object instanceof EtatLieuItem)) {
            return false;
        }
        EtatLieuItem other = (EtatLieuItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "composantVoiture:" + composantVoiture + "- garantit:" + garantit;
    }

  
    
}
