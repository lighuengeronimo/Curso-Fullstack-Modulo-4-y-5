

  var app = new Vue({
      el:"#app",
      data:{
        vueScores: [],
        vueGames: {}
      },
      methods:{
       joinGame(gameId){
        $.post("/api/game/" + gameId + "/players")
        .done(function(data) {
           window.location.href="game.html?gp=" + data.gpid;
        })
       .fail(function(){
         alert("Can't join game");
       });
         },
       returnToGame(gpId){
        window.location.href="http://localhost:8080/web/game.html?gp=" + gpId;
       }
      }
    })

  Vue.config.devtools = true;


  $.get("/api/games")
    .done(function(games) {

      app.vueGames = games;
    })
    .fail(function(jqXHR, textStatus) {
      showOutput("Failed: " + textStatus);
    });



  $.get("/api/leaderboards")
  .done (function(scores) {
    app.vueScores=scores;
    console.log(app.vueGames);
  })

  .fail(function( jqXHR, textStatus ) {
    showOutput( "Failed: " + textStatus );
  });

  function logIn(){
    $.post("/api/login", {
       userName: document.getElementById("userName").value,
       password: document.getElementById("password").value,
       })

       .done(function() {
         window.location.reload();
        })
       .fail(function() {
         alert("Incorrect username or password");
        });
   }

   function logOut(){
   $.post("/api/logout")
     .done(function() {
     window.location.reload();
     })
   }

   function signUp() {
     $.post("/api/players", {
       userName: document.getElementById("userName").value,
       password: document.getElementById("password").value
     }).done(function() {
       console.log("Signed up");
       logIn();
     })
       .fail(function(){
       alert("Username empty or already in use");
     });
   }

   function createGame(){
       $.post("/api/games", {
          })

          .done(function(data) {
            window.location.href= "game.html?gp=" + data.gpid;
            console.log(data);
            console.log("new game created!");

           })
          .fail(function() {
            alert("Error: couldn't create name");
           });
      }



