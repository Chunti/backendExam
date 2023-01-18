package rest;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.MatchDTO;
import entities.Location;
import entities.Role;
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
        User user,player1,player2;
        Location location;
        MatchDTO match;

        System.out.println(jsonString);

        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            opponentTeam = jsonObject.get("opponentTeam").getAsString();
            inDoors = Byte.parseByte(jsonObject.get("inDoors").getAsString());
            type = jsonObject.get("type").getAsString();
            user = USER_FACADE.getUser(jsonObject.get("judge").getAsString());
            location = LOCATION_FACADE.getLocation(jsonObject.get("location").getAsString());
            player1 = USER_FACADE.getUser(jsonObject.get("player1").getAsString());
            player2 = USER_FACADE.getUser(jsonObject.get("player2").getAsString());

            match = new MatchDTO(ADMIN_FACADE.createMatch(opponentTeam,user,type,inDoors,location,player1,player2));

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        String matchJSON = GSON.toJson(match);
        System.out.println(matchJSON);
        return Response.ok(matchJSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("createplayer")
    public Response createPlayer(String jsonString) throws API_Exception {
        String name, userPass, email,status;
        int phone;
        User user;
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            name = jsonObject.get("name").getAsString();
            userPass = jsonObject.get("userPass").getAsString();
            phone = jsonObject.get("phone").getAsInt();
            email = jsonObject.get("email").getAsString();
            status = jsonObject.get("status").getAsString();

            user = USER_FACADE.createPlayer(name,userPass,phone,email,status);

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        String userJSON = GSON.toJson(user);
        System.out.println(userJSON);
        return Response.ok(userJSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("createlocation")
    public Response createLocation(String jsonString) throws API_Exception {
        String address, city, condition;
        Location location;
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            address = jsonObject.get("address").getAsString();
            city = jsonObject.get("city").getAsString();
            condition = jsonObject.get("condition").getAsString();

            location = LOCATION_FACADE.createLocation(address,city,condition);

        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        String locationJSON = GSON.toJson(location);
        System.out.println(locationJSON);
        return Response.ok(locationJSON).build();
    }
}
