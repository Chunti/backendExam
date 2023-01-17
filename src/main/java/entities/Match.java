package entities;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "matches")
@NamedQuery(name = "Matches.deleteAllRows", query = "DELETE FROM Match")

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchId", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "inDoors", nullable = false)
    private Byte inDoors;

    @Size(max = 45)
    @NotNull
    @Column(name = "opponentTeam", nullable = false, length = 45)
    private String opponentTeam;

    @Size(max = 45)
    @NotNull
    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "judge", nullable = false)
    private User judge;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location", nullable = false)
    private Location location;

    @ManyToMany
    @JoinTable(name = "user_matches",
            joinColumns = @JoinColumn(name = "matchId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users = new LinkedHashSet<>();

    public Match(String opponentTeam, User judge, String type,  Byte inDoors, Location location) {
        this.opponentTeam = opponentTeam;
        this.judge = judge;
        this.type = type;
        this.inDoors = inDoors;
        this.location = location;
    }

    public Match() {
    }

    public List<String> getplayersAsStrings() {
        if (users.isEmpty()) {
            return null;
        }
        List<String> playersAsStrings = new ArrayList<>();
        users.forEach((player) -> playersAsStrings.add(player.getEmail()));
        return playersAsStrings;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getInDoors() {
        return inDoors;
    }

    public void setInDoors(Byte inDoors) {
        this.inDoors = inDoors;
    }

    public String getOpponentTeam() {
        return opponentTeam;
    }

    public void setOpponentTeam(String opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getJudge() {
        return judge;
    }

    public void setJudge(User judge) {
        this.judge = judge;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUsers(User user) {this.users.add(user);}

}