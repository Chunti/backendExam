package facades;

import entities.Location;
import entities.Match;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdminFacadeTest {

    private static EntityManagerFactory emf;
    private static AdminFacade adminFacade;

    User user,player1,player2;
    Location location1;

    public AdminFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        adminFacade = AdminFacade.getAdminFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        try {

            user = new User("user", "test123",20202020,"user@user.dk", "OK");
            player1 = new User("player1", "test123",30303030,"player1@player1.dk", "OK");
            player2 = new User("player2", "test123",33333333,"player2@player2.dk", "OK");
            User player3 = new User("player3", "test123",44444444,"player3@player3.dk", "OK");
            User admin = new User("admin", "test123",40404040,"admin@admin.dk", "OK");
            User playerUser = new User("player", "test123",30303030,"player@player.dk", "OK");

            location1 = new Location("Skinderskovvej 31", "2730 Herlev", "OK");
            Location location2 = new Location("Centrumgade 1", "2860 Ballerup", "OK");

            Match match1 = new Match("Hillerød - Herlev", user, "Squash", (byte) 1, location1);
            Match match2 = new Match("Sverige - Danmark", player1, "Squash", (byte) 0, location1);
            Match match3 = new Match("Århus - København", user, "Squash", (byte) 1, location2);

            em.getTransaction().begin();

            em.createQuery("delete from Match").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Location").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            Role playerRole = new Role("player");

            user.addRole(userRole);
            admin.addRole(adminRole);
            player1.addRole(playerRole);
            player2.addRole(playerRole);
            player3.addRole(playerRole);
            playerUser.addRole(userRole);
            playerUser.addRole(playerRole);

            player1.addMatch(match1);
            player2.addMatch(match1);

            player2.addMatch(match2);
            player3.addMatch(match2);

            player1.addMatch(match3);
            player3.addMatch(match3);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(playerRole);

            em.persist(location1);
            em.persist(location2);

            em.persist(match1);
            em.persist(match2);
            em.persist(match3);

            em.persist(user);
            em.persist(admin);
            em.persist(player1);
            em.persist(player2);
            em.persist(player3);
            em.persist(playerUser);
            em.getTransaction().commit();
        } finally {
            em.close();
        }


    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testCreateMatch() throws Exception {
        Match match = adminFacade.createMatch("TestTeam", user,"Squash", (byte) 1,location1,player1,player2 );

        assertEquals(match.getJudge(), user);

    }
}
