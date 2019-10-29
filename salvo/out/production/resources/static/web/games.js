  var app = new Vue({
      el:"#app",
      data:{
        vueGames: []
      } 
    })

  Vue.config.devtools = true;


  $.get("/api/games")
  .done (function(games) {

    app.vueGames=games;  
    console.log(app.vueGames);

  })

  .fail(function( jqXHR, textStatus ) {
    showOutput( "Failed: " + textStatus );
  });




