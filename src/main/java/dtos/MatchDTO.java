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

    byte inDoors;
    String opponentTeam;
    List<String> players;
    String type;
    JudgeDTO judge;
    LocationDTO location;

    public MatchDTO(Match match) {
        this.inDoors = match.getInDoors();
        this.opponentTeam = match.getOpponentTeam();
        this.type = match.getType();
        this.players = match.getplayersAsStrings();
        this.judge = new JudgeDTO(match.getJudge());
        this.location = new LocationDTO(match.getLocation());
    }

}