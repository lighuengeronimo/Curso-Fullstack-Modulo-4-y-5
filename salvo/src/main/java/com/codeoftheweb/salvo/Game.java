package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

import static java.util.stream.Collectors.toList;


@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy="game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Date gameDate = new Date();

    public Game() {}

    public Game(int hours) {
        int seconds = hours*3600;
        this.gameDate = Date.from(gameDate.toInstant().plusSeconds(seconds));
    }


    public Date getGameDate(){
        return this.gameDate;
    }

    public long getId(){
        return id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    @JsonIgnore
    public List<Player> getPlayer() {return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());}

    @JsonIgnore
    public Map<String, Object > makeGameDTO(){
        Map <String, Object > gameDto = new LinkedHashMap<>();
        gameDto.put("id", this.getId());
        gameDto.put("created", new Date(this.getGameDate().getTime()).toString());
        gameDto.put("players", getAllGamePlayers(this.getGamePlayers()));

        return gameDto;
    }

    @JsonIgnore
    public List<Map <String, Object>> getAllGamePlayers(Set <GamePlayer> gamePlayers){
        return gamePlayers
                .stream()
                .map(GP -> GP.makeGamePlayerDTO())
                .collect(Collectors.toList());
    }
}