package bean;

import bean.Pays;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-09T19:57:43")
@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile SingularAttribute<Client, String> localite;
    public static volatile SingularAttribute<Client, Date> dateNaissance;
    public static volatile SingularAttribute<Client, Integer> nature;
    public static volatile SingularAttribute<Client, String> cin;
    public static volatile SingularAttribute<Client, String> numroPassport;
    public static volatile SingularAttribute<Client, String> nom;
    public static volatile SingularAttribute<Client, String> rc;
    public static volatile SingularAttribute<Client, String> password;
    public static volatile SingularAttribute<Client, Date> dateInscription;
    public static volatile SingularAttribute<Client, String> numeroPermis;
    public static volatile SingularAttribute<Client, String> adresse;
    public static volatile SingularAttribute<Client, Integer> tel;
    public static volatile SingularAttribute<Client, Integer> fax;
    public static volatile SingularAttribute<Client, String> prenom;
    public static volatile SingularAttribute<Client, String> email;
    public static volatile SingularAttribute<Client, Pays> pays;

}