package facades;

import entities.Location;
import errorhandling.API_Exception;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public Location getLocation(String address) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();

        Location location;
        try {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l WHERE l.address = :address ", Location.class);
            query.setParameter("address", address);
            location = query.getSingleResult();
            if (location == null) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return location;

    }

    public List<Location> getLocations() {
        EntityManager em = emf.createEntityManager();
        List<Location> locations;
        try {
            TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l", Location.class);
            locations = query.getResultList();
        } finally {
            em.close();
        }
        return locations;
    }

    public Location createLocation(String address, String city, String condition) throws API_Exception {
        Location location = new Location(address,city,condition);
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        }catch (PersistenceException e){
            throw new API_Exception("Could not create location", 500, e);
        }finally {
            em.close();
        }

        return location;
    }
}

