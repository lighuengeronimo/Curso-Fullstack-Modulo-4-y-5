

  var app = new Vue({
      el:"#app",
      data:{
        vueGamePlayer: {}
      }
    })

  Vue.config.devtools = true;


 $.get("/api/game_view/"+getParameterByName('gp'))
   .done(function(game) {
   if (game.gameViewDTO.gamePlayers[0] == getParameterByName('gp')){
   app.vueGamePlayer = game.gameViewDTO.gamePlayers[0];
   }
   else {
   app.vueGamePlayer = game.gameViewDTO.gamePlayers[1];
   }
 })
   .fail(function(jqXHR, textStatus) {
   showOutput("Failed: " + textStatus);
 });


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
            if (data.gameViewDTO.gamePlayers[1]!=null){
                if(data.gameViewDTO.gamePlayers[0].id == getParameterByName('gp')){
                    playerInfo = [data.gameViewDTO.gamePlayers[0],data.gameViewDTO.gamePlayers[1]];
                }

                else{
                    playerInfo = [data.gameViewDTO.gamePlayers[1],data.gameViewDTO.gamePlayers[0]];
                }
            }

            else{
                playerInfo = [data.gameViewDTO.gamePlayers[0]];
            }


            $('#player1Info').text( playerInfo[0].name + '(you)');
            if (playerInfo[1]!=null){
               $('#player2Info').text( playerInfo[1].name);
            }
            else {
               $('#player2Info').text( "Waiting for player 2");
            }

            data.gameViewDTO.ships.forEach(function (shipPiece) {
                    shipPiece.locations.forEach(function (shipLocation) {
                      let turnHitted = isHit(shipLocation,data.gameViewDTO.salvoes,playerInfo[0].id)
                      if(turnHitted >0){
                        $('#B_' + shipLocation).addClass('ship-piece-hitted');
                        $('#B_' + shipLocation).text(turnHitted);
                      }
                      else
                        $('#B_' + shipLocation).addClass('ship-piece');
                    });
                  });
                  data.gameViewDTO.salvoes.sort().forEach(function (salvo) {
                    console.log(salvo);
                    if (playerInfo[0].id === salvo.player) {
                      salvo.locations.forEach(function (location) {
                        $('#S_' + location ).addClass('salvo');
                      });
                    } else {

                      salvo.locations.forEach(function (location) {
                        $('#_' + location).addClass('salvo');
                      });
                    }
                  });
        })
        .fail(function( jqXHR, textStatus ) {
           window.location.href="http://localhost:8080/web/leaderboards.html";
            alert( "Error: Wrong user // Not logged in")
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

function placeShips(gpid) {
     $.post({
       url: "/games/players/"+ gpid +"/ships",
       data: JSON.stringify({ "type": "destroyer", "locations": ["A1", "B1", "C1"]},{ "type": "patrol boat", "locations": ["H5", "H6"] }),
       dataType: "text",
       contentType: "application/json"
     })
     .done(function (response, status, jqXHR) {
       alert( "Ships placed: " + response );
     })
     .fail(function (jqXHR, status, httpError) {
       alert("Failed to place ships: " + textStatus + " " + httpError);
     });

   }
