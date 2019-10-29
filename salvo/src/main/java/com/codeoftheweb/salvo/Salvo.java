package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> locations;

    private int turn;

    public Salvo(){}

    public Salvo(GamePlayer gamePlayer, List<String> locations, int turn){
        this.gamePlayer=gamePlayer;
        this.locations=locations;
        this.turn=turn;
    }


    public long getId(){
        return id;
    }

    public int getTurn() {
        return turn;
    }
        public GamePlayer getGamePlayer () {
        return gamePlayer;
    }

    @JsonIgnore
    public List<String> getLocations() {
        return locations;
    }

    @JsonIgnore
    public Map<String, Object > makeSalvoDTO(){
        Map <String, Object > shipDto = new LinkedHashMap<>();
        shipDto.put("turn", this.getTurn());
        shipDto.put("player", this.getGamePlayer().getPlayerId());
        shipDto.put("locations", this.getLocations());
        return shipDto;
    }
}
