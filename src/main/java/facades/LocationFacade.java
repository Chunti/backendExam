package facades;

import entities.Location;
import entities.User;
import errorhandling.API_Exception;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class LocationFacade {


    private static EntityManagerFactory emf;
    private static LocationFacade instance;

    private LocationFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static LocationFacade getLocationFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new LocationFacade();
        }
        return instance;
    }

    public Location getLocation(String address, String city) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();

        Location location;
        try {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l WHERE l.address = :address AND l.city = :city", Location.class);
            query.setParameter("address", address);
            query.setParameter("city", city);
            location = query.getSingleResult();
            if (location == null) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return location;

    }
}

