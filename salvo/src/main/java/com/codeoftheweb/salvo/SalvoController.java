package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")

public class SalvoController {

    @Autowired
    private PlayerRepository playerRep;

    @Autowired
    private GameRepository gameRep;

    @Autowired
    private GamePlayerRepository gamePlayerRep;

    @Autowired
    PasswordEncoder passwordEncoder;


    @RequestMapping("/games")
    public Map <String, Object> getGames(Authentication authentication) {

        Map <String, Object > gamesDto = new LinkedHashMap<>();

        if (isGuest(authentication)){
            gamesDto.put("player", "null");
        }
        else{
            Player player = playerRep.findByUserName(authentication.getName());

            gamesDto.put("player", player.makePlayerDTO(player));
        }

        gamesDto.put("games",
            gameRep.findAll()
                    .stream()
                    .map(game -> game.makeGameDTO())
                    .collect(Collectors.toList()));

            return gamesDto;
        }

  @RequestMapping(path = "/games", method = RequestMethod.POST)
  @ResponseBody


    public ResponseEntity<Object> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Not logged in"), HttpStatus.UNAUTHORIZED);
        }

            Player player = playerRep.findByUserName(authentication.getName());

            Game newGame = new Game(0);
            GamePlayer newGP = new GamePlayer(0, player, newGame);
            gameRep.save(newGame);
            gamePlayerRep.save(newGP);

      return new ResponseEntity<>(makeMap("gpid", newGP.getId()), HttpStatus.CREATED);
    }

    private boolean isGuest(Authentication authentication) {
      return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
    @RequestMapping("/leaderboards")
    public List<Map <String, Object>> getPlayerScores(){
        return playerRep
                .findAll()
                .stream()
                .map(player -> player.makeLeaderboardsDTO())
                .collect(Collectors.toList());

    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)

    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, @RequestParam String password) {
        if ((userName.isEmpty()) || (password.isEmpty())) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.BAD_REQUEST);
        }

        Player player = playerRep.findByUserName(userName);

        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }

        Player newPlayer = playerRep.save(new Player(userName,passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("userName", newPlayer.getUserName()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGamePlayerViewByID(@PathVariable Long gamePlayerId, Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRep.getOne(gamePlayerId);

        Player player = playerRep.findByUserName(authentication.getName());

        if ( gamePlayer.getPlayer().getId()== player.getId() ){
            return new ResponseEntity<>(makeMap("gameViewDTO",gamePlayer.makeGameViewDTO()), HttpStatus.CREATED );
        }
        else {
            return new ResponseEntity<>(makeMap("error", "Wrong user for game view"), HttpStatus.UNAUTHORIZED);

        }
    }




    }


