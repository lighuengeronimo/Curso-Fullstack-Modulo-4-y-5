package com.codeoftheweb.salvo;

        import com.fasterxml.jackson.annotation.JsonIgnore;
        import org.hibernate.annotations.GenericGenerator;

        import javax.persistence.*;
        import javax.persistence.FetchType;
        import java.util.*;



@Entity
public class Ship {
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

    private String shipType;

    public Ship() { }

    public Ship(String shipType,List<String> locations, GamePlayer GP) {
        this.shipType = shipType;
        this.locations = locations;
        this.gamePlayer = GP;
    }


    public String getType(){
        return shipType;
    }

    public long getId(){
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    @JsonIgnore
    public List<String> getLocations() {
        return locations;
    }

    @JsonIgnore
    public Map<String, Object > makeShipDTO(){
        Map <String, Object > shipDto = new LinkedHashMap<>();
        shipDto.put("id", this.getId());
        shipDto.put("type", this.getType());
        shipDto.put("locations", this.getLocations());
        return shipDto;
    }
}

