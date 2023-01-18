package rest;

import com.google.gson.Gson;
import dtos.LocationDTO;
import dtos.MatchDTO;
import errorhandling.NotFoundException;
import facades.LocationFacade;
import facades.MatchFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("location")

public class LocationResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final LocationFacade LOCATION_FACADE= LocationFacade.getLocationFacade(EMF);
    private final Gson GSON = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public Response allLocations() {

        List<LocationDTO> locationDTOS;
        locationDTOS = LOCATION_FACADE.getLocations().stream()
                .map(LocationDTO::new)
                .collect(Collectors.toList());
        String locationJSON = GSON.toJson(locationDTOS);
        System.out.println(locationJSON);
        return Response.ok(locationJSON).build();
    }


}
