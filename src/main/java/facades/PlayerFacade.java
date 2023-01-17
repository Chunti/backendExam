package facades;

import entities.Match;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PlayerFacade {

    private static EntityManagerFactory emf;
    private static PlayerFacade instance;

    private PlayerFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static PlayerFacade getPlayerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PlayerFacade();
        }
        return instance;
    }







}
