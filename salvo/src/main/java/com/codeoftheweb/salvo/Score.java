package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

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

    double score;

    Date finishDate;

    public Score(){}

    public Score(Player player, Game game, double score, int hours){
        this.player=player;
        this.game=game;
        this.score=score;
        this.finishDate = Date.from(new Date().toInstant().plusSeconds(hours*3600));
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public double getScore() {
        return score;
    }

    @JsonIgnore
    public Date getFinishDate() {
        return finishDate;
    }
}
