package rest;

import entities.Location;
import entities.Match;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.testng.AssertJUnit.assertFalse;

public class MatchEndpointTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        try {

            User user = new User("user", "test123",20202020,"user@user.dk", "OK");
            User player1 = new User("player1", "test123",30303030,"player1@player1.dk", "OK");
            User player2 = new User("player2", "test123",33333333,"player2@player2.dk", "OK");
            User player3 = new User("player3", "test123",44444444,"player3@player3.dk", "OK");
            User admin = new User("admin", "test123",40404040,"admin@admin.dk", "OK");
            User playerUser = new User("player", "test123",30303030,"player@player.dk", "OK");

            Location location1 = new Location("Skinderskovvej 31", "2730 Herlev", "OK");
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

    private static String securityToken;


    private static void login(String email, String password) {
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }


    @Test
    public void testGetAllMatches() {
        String email = "user@user.dk";
        String password = "test123";

        login(email, password);
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/match/all").then()
                .statusCode(200)
                .body("inDoors", hasItem(1))
                .body("opponentTeam", hasItem("Hillerød - Herlev"))
                .body("locationAddress", hasItem("Skinderskovvej 31"))
                .body("locationCity", hasItem("2860 Ballerup"));
    }

    @Test
    public void testGetPlayerMatches() {
        String email = "player1@player1.dk";
        String password = "test123";

        login(email, password);
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/match/player").then()
                .statusCode(200)
                .body("opponentTeam", hasItem("Hillerød - Herlev"))
                .body("opponentTeam", hasItem("Århus - København"));

    }

    @Test
    public void testDoesNotContainPlayerMatches() {
        String email = "player1@player1.dk";
        String password = "test123";

        login(email, password);
        Response response =
                given()
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when()
                        .get("/match/player");

        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();
        List<String> opponentTeam = jsonPath.getList("opponentTeam");

        assertFalse(opponentTeam.contains("Sverige - Danmark"));
    }

}
