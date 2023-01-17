package dtos;

import entities.Location;
import entities.User;

import java.util.List;

public class LocationDTO {


    String address;
    String city;
    String condition;


    public LocationDTO(Location location) {
        this.address = location.getAddress();
        this.city = location.getCity();
        this.condition = location.getCondition();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCondition() {
        return condition;
    }
}
