package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "`match`")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchId", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "oppponentTeam", nullable = false, length = 45)
    private String oppponentTeam;

    @Size(max = 45)
    @Column(name = "judge", length = 45)
    private String judge;

    @Size(max = 45)
    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "inDoors")
    private Byte inDoors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Location location;

    @ManyToMany
    @JoinTable(name = "user_matches",
            joinColumns = @JoinColumn(name = "matchId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOppponentTeam() {
        return oppponentTeam;
    }

    public void setOppponentTeam(String oppponentTeam) {
        this.oppponentTeam = oppponentTeam;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Byte getInDoors() {
        return inDoors;
    }

    public void setInDoors(Byte inDoors) {
        this.inDoors = inDoors;
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

}