package facades;

import com.google.gson.JsonElement;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import errorhandling.API_Exception;
import org.mindrot.jbcrypt.BCrypt;
import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String email, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            user = query.getSingleResult();
            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User getUser(String email) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            user = query.getSingleResult();
            if (user == null /*|| !user.verifyPassword(password)*/) {
                throw new AuthenticationException("Faulty token");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(String name, String password,int phone,String email,String status) throws API_Exception {


        // Construct user:
        User user = new User(name, password,phone,email,status);

        // Persist user to database:
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            Role role = em.find(Role.class, "user");
            if (role == null) {
                role = new Role("user");
                em.persist(role);
            }
            user.addRole(role);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new API_Exception("Could not create user", 500, e);
        } finally {
            em.close();
        }

        return user;
    }

}

