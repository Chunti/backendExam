package facades;

import dtos.MatchDTO;
import entities.Match;
import entities.User;
import errorhandling.NotFoundException;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class MatchFacade {

    private static EntityManagerFactory emf;
    private static MatchFacade instance;

    private MatchFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static MatchFacade getMatchFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MatchFacade();
        }
        return instance;
    }

    public List<Match> getAllMatches() throws NotFoundException  {
        EntityManager em = emf.createEntityManager();
        List<Match> matches;
        try {
            TypedQuery<Match> query = em.createQuery("SELECT m FROM Match m ", Match.class);
            matches = query.getResultList();
            if (matches == null) {
                throw new NotFoundException("No Matches was found");
            }
        } finally {
            em.close();
        }
        return matches;
    }



}
