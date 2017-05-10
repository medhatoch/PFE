package bean;

import bean.Client;
import bean.Manager;
import bean.ReservationItem;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-09T19:57:43")
@StaticMetamodel(Reservation.class)
public class Reservation_ { 

    public static volatile ListAttribute<Reservation, ReservationItem> reservationItems;
    public static volatile SingularAttribute<Reservation, Manager> manager;
    public static volatile SingularAttribute<Reservation, Client> client;
    public static volatile SingularAttribute<Reservation, Long> id;
    public static volatile SingularAttribute<Reservation, Date> dateReservation;
    public static volatile SingularAttribute<Reservation, Double> prixTotal;

}