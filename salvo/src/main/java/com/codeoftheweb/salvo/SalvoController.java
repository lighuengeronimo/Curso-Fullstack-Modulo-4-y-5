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

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")

public class SalvoController {

  @Autowired
  private ShipRepository shipRep;

  @Autowired
  private PlayerRepository playerRep;

  @Autowired
  private GameRepository gameRep;

  @Autowired
  private GamePlayerRepository gamePlayerRep;

  @Autowired
  PasswordEncoder passwordEncoder;


  @RequestMapping("/games")
  public Map<String, Object> getGames(Authentication authentication) {

    Map<String, Object> gamesDto = new LinkedHashMap<>();

    if (isGuest(authentication)) {
      gamesDto.put("player", "null");
    } else {
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
  public List<Map<String, Object>> getPlayerScores() {
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

    Player newPlayer = playerRep.save(new Player(userName, passwordEncoder.encode(password)));
    return new ResponseEntity<>(makeMap("userName", newPlayer.getUserName()), HttpStatus.CREATED);
  }

  private Map<String, Object> makeMap(String key, Object value) {
    Map<String, Object> map = new HashMap<>();
    map.put(key, value);
    return map;
  }

  @RequestMapping("/game_view/{gamePlayerId}")
  public ResponseEntity<Map<String, Object>> getGamePlayerViewByID(@PathVariable Long gamePlayerId, Authentication authentication) {


    GamePlayer gamePlayer = gamePlayerRep.getOne(gamePlayerId);

    Player player = playerRep.findByUserName(authentication.getName());

    if (gamePlayer.getPlayer().getId() == player.getId()) {
      return new ResponseEntity<>(makeMap("gameViewDTO", gamePlayer.makeGameViewDTO()), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(makeMap("error", "Wrong user for game view"), HttpStatus.UNAUTHORIZED);

    }
  }

  @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> getGameViewByID(@PathVariable Long gameId, Authentication authentication) {

    if (isGuest(authentication)) {
      return new ResponseEntity<>(makeMap("error", "Not logged in"), HttpStatus.UNAUTHORIZED);
    }

    if (gameRep.getOne(gameId) == null) {
      return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.UNAUTHORIZED);
    }

    Game game = gameRep.getOne(gameId);

    if (game.getGamePlayers().stream().count() > 1) {
      return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.UNAUTHORIZED);
    }


    Player player = playerRep.findByUserName(authentication.getName());


    if (game.getGamePlayers().stream().map(gp -> gp.getPlayer().getUserName()).collect(Collectors.toList()).contains(player.getUserName())) {
      return new ResponseEntity<>(makeMap("Error", "You are already in this game"), HttpStatus.FORBIDDEN);
    }
    GamePlayer newGP = new GamePlayer(0, player, game);
    gamePlayerRep.save(newGP);
    return new ResponseEntity<>(makeMap("gpid", newGP.getId()), HttpStatus.CREATED);
  }

  @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
  public ResponseEntity<Object> addShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication authentication) {

    if (isGuest(authentication)) {
      return new ResponseEntity<>(makeMap("error", "Not logged in"), HttpStatus.UNAUTHORIZED);
    }
    Optional<GamePlayer> OPTgp = gamePlayerRep.findById(gamePlayerId);

    if (!OPTgp.isPresent()) {
      return new ResponseEntity<>(makeMap("error", "No gameplayer found"), HttpStatus.UNAUTHORIZED);
    }

    Player player = playerRep.findByUserName(authentication.getName());
    GamePlayer gamePlayer = OPTgp.get();

    if (gamePlayer.getPlayer().getId() != player.getId()) {
      return new ResponseEntity<>(makeMap("error", "Wrong user for game view"), HttpStatus.UNAUTHORIZED);
    }

    if (gamePlayer.getShips().size()>0) {
      return new ResponseEntity<>(makeMap("error", "Ships already placed"), HttpStatus.FORBIDDEN);
    }

    if (ships.size() == 0 || ships.size() != 5) {
      return new ResponseEntity<>(makeMap("error", "You need to place all 5 ships!"), HttpStatus.FORBIDDEN);
    }
      else {
        ships.stream().forEach(ship -> gamePlayer.addShip(new Ship(ship.getType(), ship.getLocations(), gamePlayer)));
        shipRep.saveAll(ships);
        return new ResponseEntity<>(makeMap("Success", "Ships succesfully placed"), HttpStatus.CREATED);

      }

    }

  }
}



