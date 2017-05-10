package bean;

import bean.Reservation;
import bean.Vehicule;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-09T19:57:43")
@StaticMetamodel(ReservationItem.class)
public class ReservationItem_ { 

    public static volatile SingularAttribute<ReservationItem, Vehicule> vehicule;
    public static volatile SingularAttribute<ReservationItem, Date> dateRetour;
    public static volatile SingularAttribute<ReservationItem, Reservation> reservation;
    public static volatile SingularAttribute<ReservationItem, Long> id;
    public static volatile SingularAttribute<ReservationItem, Double> prixReservation;

}