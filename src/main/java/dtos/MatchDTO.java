package dtos;

import entities.Location;
import entities.Match;
import entities.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Match} entity
 */
public class MatchDTO implements Serializable {
    @NotNull
    private final Byte inDoors;
    @Size(max = 45)
    @NotNull
    private final String opponentTeam;
    @Size(max = 45)
    @NotNull
    private final String type;
    private final Integer judgeId;
    @Size(max = 25)
    @NotNull
    private final String judgeName;
    @NotNull
    private final Integer judgePhone;
    @Size(max = 45)
    @NotNull
    private final String judgeEmail;
    @Size(max = 45)
    @NotNull
    private final String judgeStatus;
    private final Integer locationId;
    @Size(max = 45)
    @NotNull
    private final String locationAddress;
    @Size(max = 45)
    @NotNull
    private final String locationCity;
    @Size(max = 255)
    @NotNull
    private final String locationCondition;

    public MatchDTO(Match match) {
        this.inDoors = match.getInDoors();
        this.opponentTeam = match.getOpponentTeam();
        this.type = match.getType();
        this.judgeId = match.getJudge().getId();
        this.judgeName = match.getJudge().getName();
        this.judgePhone = match.getJudge().getPhone();
        this.judgeEmail = match.getJudge().getEmail();
        this.judgeStatus = match.getJudge().getStatus();
        this.locationId = match.getLocation().getId();
        this.locationAddress = match.getLocation().getAddress();
        this.locationCity = match.getLocation().getCity();
        this.locationCondition = match.getLocation().getCondition();
    }

    public MatchDTO(Byte inDoors, String opponentTeam, String type, User user, Location location) {
        this.inDoors = inDoors;
        this.opponentTeam = opponentTeam;
        this.type = type;
        this.judgeId = user.getId();
        this.judgeName = user.getName();
        this.judgePhone = user.getPhone();
        this.judgeEmail = user.getEmail();
        this.judgeStatus = user.getStatus();
        this.locationId = location.getId();
        this.locationAddress = location.getAddress();
        this.locationCity = location.getCity();
        this.locationCondition = location.getCondition();
    }

    public Byte getInDoors() {
        return inDoors;
    }

    public String getOpponentTeam() {
        return opponentTeam;
    }

    public String getType() {
        return type;
    }

    public Integer getJudgeId() {
        return judgeId;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public Integer getJudgePhone() {
        return judgePhone;
    }

    public String getJudgeEmail() {
        return judgeEmail;
    }

    public String getJudgeStatus() {
        return judgeStatus;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public String getLocationCondition() {
        return locationCondition;
    }



    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "inDoors = " + inDoors + ", " +
                "opponentTeam = " + opponentTeam + ", " +
                "type = " + type + ", " +
                "judgeId = " + judgeId + ", " +
                "judgeName = " + judgeName + ", " +
                "judgePhone = " + judgePhone + ", " +
                "judgeEmail = " + judgeEmail + ", " +
                "judgeStatus = " + judgeStatus + ", " +
                "locationId = " + locationId + ", " +
                "locationAddress = " + locationAddress + ", " +
                "locationCity = " + locationCity + ", " +
                "locationCondition = " + locationCondition + ")";
    }
}