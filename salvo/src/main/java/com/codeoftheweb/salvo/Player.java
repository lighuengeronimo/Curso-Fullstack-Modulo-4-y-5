package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private String userName;

    private String password;

    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName(){
        return userName;
    }

    public long getId(){
        return id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Score getScore(Game gameScore) {
        return  this.getScores().stream().filter(score -> score.getGame() == gameScore).findFirst().orElse(null);
    }

    public long getVictories(){
        return getScores().stream().filter(score -> score.getScore()== 1).count();
    }

    public long getDefeats(){
        return getScores().stream().filter(score -> score.getScore()== 0).count();
    }

    public long getTies(){
        return getScores().stream().filter(score -> score.getScore()== 0.5).count();
    }

    public double getTotalScore(){
        return (double) getVictories()  + (getTies()*0.5);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    @JsonIgnore
    public List<Game> getGame() {return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());}

    @JsonIgnore
    public Map<String, Object > makePlayerDTO(Player player){
        Map <String, Object > playerDto = new LinkedHashMap<>();
        playerDto.put("id", player.getId());
        playerDto.put("name", player.getUserName());
        return playerDto;
    }

    @JsonIgnore
    public Map<String, Object > makeLeaderboardsDTO(){
        Map <String, Object > leaderboardsDto = new LinkedHashMap<>();
        leaderboardsDto.put("player", this.getUserName());
        leaderboardsDto.put("victories", this.getVictories());
        leaderboardsDto.put("defeats", this.getDefeats());
        leaderboardsDto.put("ties", this.getTies());
        leaderboardsDto.put("total_score", this.getTotalScore());
        return leaderboardsDto;
    }

}
