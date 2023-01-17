package facades;

import entities.Location;
import entities.Match;
import entities.User;
import errorhandling.API_Exception;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public class AdminFacade {


    private static EntityManagerFactory emf;
    private static AdminFacade instance;

    private AdminFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
        }
        return instance;
    }

    public Match createMatch(String opponentTeam, User user, String type, byte inDoors, Location location, User p1, User p2) throws API_Exception {

        Match match = new Match(opponentTeam,user,type,inDoors,location);
        match.addUsers(p1);
        match.addUsers(p2);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(match);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new API_Exception("Could not create user", 500, e);
        } finally {
            em.close();
        }
        return match;
    }
}