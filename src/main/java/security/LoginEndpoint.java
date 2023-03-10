package security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;
import dtos.UserDTO;
import facades.UserFacade;

import java.util.logging.Level;
import java.util.logging.Logger;

import errorhandling.API_Exception;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import security.errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

@Path("login")
public class LoginEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonString) throws AuthenticationException, API_Exception {
        String email, password;

        System.out.println(jsonString);
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            email = json.get("email").getAsString();
            password = json.get("password").getAsString();

        } catch (Exception e) {
           throw new API_Exception("Malformed JSON Supplied", 400, e);
        }
        System.out.println("email: " + email);
        System.out.println("Password: " + password);

        try {
            UserDTO user = new UserDTO(USER_FACADE.getVeryfiedUser(email, password));
            SignedJWT token = Token.createToken(email, user.getRoles(), user.getId());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("email", email);
            responseJson.addProperty("token", token.serialize());
            return Response.ok(responseJson.toString()).build();
        } catch (JOSEException | AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                throw (AuthenticationException) ex;
            }
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new AuthenticationException("Invalid username or password! Please try again");
    }
}
