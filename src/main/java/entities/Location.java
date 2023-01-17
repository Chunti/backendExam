package entities;

import com.google.gson.JsonObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "location")
@NamedQuery(name = "Location.deleteAllRows", query = "DELETE FROM Location")

public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locationId", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "address", length = 45)
    private String address;

    @Size(max = 45)
    @NotNull
    @Column(name = "city", length = 45)
    private String city;

    @Size(max = 255)
    @NotNull
    @Column(name = "`condition`")
    private String condition;

    @OneToMany(mappedBy = "location")
    private Set<Match> matches = new LinkedHashSet<>();

    public Location(String address, String city, String condition) {
        this.address = address;
        this.city = city;
        this.condition = condition;
    }
    public Location() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

}