$(function() {
    loadData();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            console.log(data)
            let playerInfo;
            if(data.gamePlayers[0].id == getParameterByName('gp')){
                playerInfo1 = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];
            }

            else{
                playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];
            }

            $('#player1Info').text(playerInfo[0] + '(you)');
            $('#player2Info').text( playerInfo[1]);

            data.ships.forEach(function (shipPiece) {
                    shipPiece.locations.forEach(function (shipLocation) {
                      let turnHitted = isHit(shipLocation,data.salvoes,playerInfo[0].id)
                      if(turnHitted >0){
                        $('#' + shipLocation).addClass('ship-piece-hited');
                        $('#' + shipLocation).text(turnHitted);
                      }
                      else
                        $('#' + shipLocation).addClass('ship-piece');
                    });
                  });
                  data.salvoes.forEach(function (salvo) {
                    console.log(salvo);
                    if (playerInfo[0].id === salvo.player) {
                      salvo.locations.forEach(function (location) {
                        $('#' + location + "_2").addClass('salvo');
                      });
                    } else {
                      salvo.locations.forEach(function (location) {
                        $('#' + location+ "_2").addClass('salvo');
                      });
                    }
                  });
        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
    }

function isHit(shipLocation,salvoes,playerId) {
  var hit = 0;
  salvoes.forEach(function (salvo) {
    if(salvo.player != playerId)
      salvo.locations.forEach(function (location) {
        if(shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit;
}
