package rest;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.UserDTO;
import entities.Location;
import entities.User;
import errorhandling.API_Exception;
import facades.AdminFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/*@Path("match")
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AdminFacade ADMIN_FACADE = AdminFacade.getAdminFacade(EMF);
    private final Gson GSON = new Gson();




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonString) throws API_Exception {
        String opponentTeam, type;
        byte inDoors;
        User user;
        Location location;
        int phone;
        List<String> roles = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            opponentTeam = jsonObject.get("opponentTeam").getAsString();
            inDoors = jsonObject.get("inDoors").getAsByte();
            type = jsonObject.get("type").getAsString();
            email = jsonObject.get("email").getAsString();
            status = jsonObject.get("status").getAsString();
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        UserDTO user = new UserDTO(USER_FACADE.createUser(username,password,phone,email,status));
        String userJSON = GSON.toJson(user);
        System.out.println(userJSON);
        return Response.ok(userJSON).build();
    }





}*/
