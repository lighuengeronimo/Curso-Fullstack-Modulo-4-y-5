package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();


    private Date joinDate;

    public GamePlayer(){}

    public GamePlayer ( int hours, Player player, Game game){
        int seconds = hours*3600;
        this.joinDate = Date.from(new Date().toInstant().plusSeconds(seconds));
        this.player=player;
        this.game=game;
    }



    public Date getDate(){
        return this.joinDate;
    }


    @JsonIgnore
    public Player getPlayer(){
        return player;
    }


    @JsonIgnore
    public Game getGame(){
        return game;
    }


    public void addShip(Ship ship){
       ships.add(ship);
    }

    @JsonIgnore
    public Set<Ship> getShips(){
        return ships;
    }

    @JsonIgnore
    public Set<Salvo> getSalvoes(){
        return salvoes;
    }

    public String getUserNames(){
        return player.getUserName();
    }

    public long getPlayerId(){
        return player.getId();
    }

    public long getGameId(){
        return game.getId();
    }

    public Score getGPScore(){
        return this.getPlayer().getScore(this.game);
    }

    public long getId(){
        return id;
    }

    @JsonIgnore
    public Map<String, Object > makeGamePlayerDTO(){
        Map <String, Object > gamePlayerDto = new LinkedHashMap<>();
        gamePlayerDto.put("gpid", this.getId());
        gamePlayerDto.put("id", this.getPlayer().getId());
        gamePlayerDto.put("name", this.getPlayer().getUserName());

        return gamePlayerDto;
    }

    @JsonIgnore
    public Map<String, Object > makeGameViewDTO(){
        Map <String, Object > gameViewDto = new LinkedHashMap<>();
        gameViewDto.put("id", this.game.getId());
        gameViewDto.put("created", new Date(this.game.getGameDate().getTime()).toString());
        gameViewDto.put("gamePlayers", this.game.getAllGamePlayers(this.game.getGamePlayers()));
        gameViewDto.put("ships", getMyShips(this.getShips()));
        gameViewDto.put("salvoes", this.game.getGamePlayers().stream()
                                                            .flatMap(gp->gp.getSalvoes().stream().map(salvo->salvo.makeSalvoDTO())).collect(Collectors.toList()));
        return gameViewDto;
    }

    @JsonIgnore
    public List<Map <String, Object>> getMyShips(Set <Ship> ships){
        return ships
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList());
    }
}


