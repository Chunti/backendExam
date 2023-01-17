package rest;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.MatchDTO;
import entities.Location;
import entities.User;
import errorhandling.API_Exception;
import facades.AdminFacade;
import facades.LocationFacade;
import facades.UserFacade;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("admin")
@RolesAllowed("admin")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AdminFacade ADMIN_FACADE = AdminFacade.getAdminFacade(EMF);
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private static final LocationFacade LOCATION_FACADE = LocationFacade.getLocationFacade(EMF);
    private final Gson GSON = new Gson();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("creatematch")
    public Response createMatch(String jsonString) throws API_Exception, AuthenticationException {
        String opponentTeam, type;
        byte inDoors;
        User user;
        Location location;
        JsonArray playerArray;
        List<User> players = new ArrayList<>();
        MatchDTO match;

        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            opponentTeam = jsonObject.get("opponentTeam").getAsString();
            inDoors = jsonObject.get("inDoors").getAsByte();
            type = jsonObject.get("type").getAsString();
            user = USER_FACADE.getUser(jsonObject.get("judge.email").getAsString());
            location = LOCATION_FACADE.getLocation(jsonObject.get("location.address").getAsString(),
                                                    jsonObject.get("location.city").getAsString());
            playerArray = jsonObject.getAsJsonArray("players");


            for (int i = 0; i < playerArray.size(); i++) {
                players.add(USER_FACADE.getUser(playerArray.get(i).getAsString()));
            }
            match = new MatchDTO(ADMIN_FACADE.createMatch(opponentTeam,user,type,inDoors,location,players.get(0),players.get(1)));

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        String matchJSON = GSON.toJson(match);
        System.out.println(matchJSON);
        return Response.ok(matchJSON).build();
    }
}
