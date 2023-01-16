package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@NamedQuery(name = "User.deleteAllRows", query = "DELETE FROM User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 25)
    @NotNull
    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_pass", nullable = false)
    private String userPass;

    @NotNull
    @Column(name = "phone")
    private Integer phone;

    @NotNull
    @Size(max = 45)
    @Column(name = "email", length = 45)
    private String email;

    @Size(max = 45)
    @NotNull
    @Column(name = "status", length = 45)
    private String status;

    @ManyToMany
    @JoinTable(name = "user_matches",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "matchId"))
    private Set<Match> matches = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Set<Role> roles = new LinkedHashSet<>();

    public User() {
    }

    public User(String name, String userPass, Integer phone, String email, String status) {
        this.name = name;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public List<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roles.forEach((role) -> rolesAsStrings.add(role.getRoleName()));
        return rolesAsStrings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean verifyPassword(String pw){
        return BCrypt.checkpw(pw,userPass);
    }
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public void addRole(Role userRole) {
        roles.add(userRole);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}