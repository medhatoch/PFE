/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Manager;
import controler.util.HashageUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class ManagerFacade extends AbstractFacade<Manager> {

    @PersistenceContext(unitName = "PFEPU")
    private EntityManager em;
    int trys;

    public int createManager(Manager manager) {
        Manager loaded = find(manager.getLogin());
        if (loaded != null) {
            return -1;
        } else if (manager.getLogin().equals("")) {
            return -2;
        } else if (manager.getPassword().equals("")) {
            return -3;
        }
        manager.setPassword(HashageUtil.sha256(manager.getPassword()));
        create(manager);
        return 1;
    }

    public int connect(String login, String pass) {
        Manager loaded = find(login);
        if (loaded == null) {
            return -1;
        } else {
            if (loaded.getPassword().equals(HashageUtil.sha256(pass))) {
                return 1;
            }
            return -2;
        }
    }

    public int seConnecter(Manager manager) {
        trys = 0;
        Manager loaded = find(manager.getLogin());
        if (loaded == null) {
            trys++;
            return -1;
        }
        if (loaded.getBlocked() == 1) {
            return -2;
        }
        if (loaded != null) {
            if (!loaded.getPassword().equals(HashageUtil.sha256(manager.getPassword()))) {
                trys++;
                return -3;
            } else {
                trys = 0;
                return 1;
            }
        }
        if (trys == 3) {
            loaded.setBlocked(1);
            edit(loaded);
        }
        return 0;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManagerFacade() {
        super(Manager.class);
    }

}
