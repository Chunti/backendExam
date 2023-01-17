package rest;

import com.google.gson.Gson;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.*;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import errorhandling.API_Exception;
import utils.EMF_Creator;
import utils.Populator;

@Path("info")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    Gson gson = new Gson();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    public DemoResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("player")
    @RolesAllowed("player")
    public String getFromPlayer() {
        String thisuser = securityContext.getUserPrincipal().getName();
        String thisrole = "player";

        return "{\"email\": \"" + thisuser + "\",\"role\":\"" + thisrole + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        String thisrole = "admin";

        return "{\"email\": \"" + thisuser + "\",\"role\":\"" + thisrole + "\"}";
    }

    @GET
    @Path("/populate")
    @Produces({MediaType.APPLICATION_JSON})
    public Response populate() {
        try {
            Populator.populate();
            System.out.println("populated");
        } catch (API_Exception e) {
            throw new RuntimeException(e);
        }

        String ok = gson.toJson("OK");
        return Response.ok(ok).build();
    }

}