package controler.util;


import bean.Client;
import bean.Manager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final SessionUtil instance = new SessionUtil();

    private SessionUtil() {
    }

    private static List<Manager> users = new ArrayList();

    private static boolean isConnected(Manager user) {
        if (users.stream().anyMatch((x) -> (x.getLogin().equals(user.getLogin())))) {
            return true;
        }
        return false;
    }

//    public static void attachUserToCommune(User user) {
//        Commune myCommune = user.getCommune();
//        if (myCommune.getUsers().indexOf(user) == -1) {
//            myCommune.getUsers().add(user);
//        }
//        registerUser(user);
//    }
    public static void registerManager(Manager user) {
        setAttribute("user", user);
        if (!isConnected(user)) {
            users.add(user);
        }
    }

    public static void unSetManager(Manager user) {
        setAttribute("user", null);
        users.remove(user);
    }

    public static Manager getConnectedManager() {
        return (Manager) getAttribute("user");
    }

    public static void registerClient(Client redevable) {
        setAttribute("redevable", redevable);
    }

    public static Client getConnectedClient() {
        return (Client) getAttribute("redevable");
    }

 

    public static SessionUtil getInstance() {
        return instance;
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static void redirect(String pagePath) throws IOException {
        if (!pagePath.endsWith(".xhtml")) {
            pagePath += ".xhtml";
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(pagePath);

    }

    private static boolean isContextOk(FacesContext fc) {
        boolean res = (fc != null
                && fc.getExternalContext() != null
                && fc.getExternalContext().getSession(false) != null);
        return res;
    }

    private static HttpSession getSession(FacesContext fc) {
        return (HttpSession) fc.getExternalContext().getSession(false);
    }

    public static Object getAttribute(String cle) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Object res = null;
        if (isContextOk(fc)) {
            res = getSession(fc).getAttribute(cle);
        }
        return res;
    }

    public static void setAttribute(String cle, Object valeur) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null && fc.getExternalContext() != null) {
            getSession(fc).setAttribute(cle, valeur);
        }
    }
}
