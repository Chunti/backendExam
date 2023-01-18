package rest;

import com.google.gson.Gson;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dtos.MatchDTO;

import errorhandling.NotFoundException;
import facades.MatchFacade;

import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import java.util.stream.Collectors;


@Path("match")
public class MatchResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final MatchFacade MATCH_FACADE= MatchFacade.getMatchFacade(EMF);
    private final Gson GSON = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }


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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("player")
    public Response PlayerMatches(@HeaderParam("x-access-token") String token)throws Exception {


        System.out.println(token);
        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        int userId = claims.getIntegerClaim("id");

        List<MatchDTO> matchDTOs;
        try {
            matchDTOs = MATCH_FACADE.getMatchesForPlayer(userId).stream()
                    .map(MatchDTO::new)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        String matchesJSON = GSON.toJson(matchDTOs);
        return Response.ok(matchesJSON).build();
    }

}
