package rest;

import com.google.gson.Gson;
import dtos.MatchDTO;
import dtos.UserDTO;
import entities.Match;
import entities.User;
import errorhandling.NotFoundException;
import facades.MatchFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Path("match")
public class MatchResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final MatchFacade MATCH_FACADE= MatchFacade.getMatchFacade(EMF);
    private final Gson GSON = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public Response allMatches() {

        List<MatchDTO> matchDTOs;
        try {
            matchDTOs = MATCH_FACADE.getAllMatches().stream()
                    .map(MatchDTO::new)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        String matchesJSON = GSON.toJson(matchDTOs);
        System.out.println(matchesJSON);
        return Response.ok(matchesJSON).build();
    }

}
