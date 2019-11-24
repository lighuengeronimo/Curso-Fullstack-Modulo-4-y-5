package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(PlayerRepository PlayerRep, GameRepository GameRep,
									  GamePlayerRepository GPRep, ShipRepository ShipRep,
									  SalvoRepository SalvoRep, ScoreRepository ScoreRep) {
		return (args) -> {
			// save a couple of customers
			Player player1 = new Player("j.bauer@ctu.gov",passwordEncoder().encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov",passwordEncoder().encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com",passwordEncoder().encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov",passwordEncoder().encode("mole"));

			Game game1 = new Game();
			Game game2 = new Game(1);
			Game game3 = new Game(2);
			Game game4 = new Game(3);
			Game game5 = new Game(4);
			Game game6 = new Game(5);
			Game game7 = new Game(6);
			Game game8 = new Game(7);

			GamePlayer gamePlayer1 = new GamePlayer(0, player1,game1);
			GamePlayer gamePlayer2 = new GamePlayer(0, player2,game1);
			GamePlayer gamePlayer3 = new GamePlayer(1, player1,game2);
			GamePlayer gamePlayer4 = new GamePlayer(1, player2,game2);
			GamePlayer gamePlayer5 = new GamePlayer(2, player2,game3);
			GamePlayer gamePlayer6 = new GamePlayer(2, player4,game3);
			GamePlayer gamePlayer7 = new GamePlayer(3, player2,game4);
			GamePlayer gamePlayer8 = new GamePlayer(3, player1,game4);
			GamePlayer gamePlayer9 = new GamePlayer(4, player4,game5);
			GamePlayer gamePlayer10 = new GamePlayer(4, player1,game5);
			GamePlayer gamePlayer11 = new GamePlayer(5, player3,game6);
			GamePlayer gamePlayer12 = new GamePlayer(6, player4,game7);
			GamePlayer gamePlayer13= new GamePlayer(7, player3,game8);
			GamePlayer gamePlayer14= new GamePlayer(7, player4,game8);


			Ship ship1 =new Ship("Destroyer", Arrays.asList("H2", "H3", "H4"), gamePlayer1);
			Ship ship2 =new Ship("Submarine", Arrays.asList("E1", "F1", "G1"),gamePlayer1);
			Ship ship3 =new Ship("Patrol_Boat", Arrays.asList("B4", "B5"),gamePlayer1);
			Ship ship4 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"),gamePlayer2);
			Ship ship5 =new Ship("Patrol_Boat", Arrays.asList("F1", "F2"),gamePlayer2);
			Ship ship6 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"),gamePlayer3);
			Ship ship7 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"),gamePlayer3);
			Ship ship8 =new Ship("Submarine", Arrays.asList("A2", "A3", "A4"),gamePlayer4);
			Ship ship9 =new Ship("Patrol_Boat", Arrays.asList("G6", "H6"),gamePlayer4);
			Ship ship10 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"),gamePlayer5);
			Ship ship11 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"),gamePlayer5);
			Ship ship12 =new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), gamePlayer6);
			Ship ship13 =new Ship("Patrol_Boat", Arrays.asList("G6", "H6"), gamePlayer6);
			Ship ship14 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gamePlayer7);
			Ship ship15 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"), gamePlayer7);
			Ship ship16 =new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), gamePlayer8);
			Ship ship17 =new Ship("Patrol_Boat", Arrays.asList("G6", "H6"), gamePlayer8);
			Ship ship18 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gamePlayer9);
			Ship ship19 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"), gamePlayer9);
			Ship ship20 =new Ship("Submarine", Arrays.asList("H2", "H3", "H4"), gamePlayer10);
			Ship ship21 =new Ship("Patrol_Boat", Arrays.asList("H2", "H3", "H4"), gamePlayer10);
			Ship ship22 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gamePlayer11);
			Ship ship23 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"), gamePlayer11);
			Ship ship24 =new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), gamePlayer13);
			Ship ship25 =new Ship("Patrol_Boat", Arrays.asList("C6", "C7"), gamePlayer13);
			Ship ship26 =new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), gamePlayer14);
			Ship ship27 =new Ship("Patrol_Boat", Arrays.asList("G6", "H6"), gamePlayer14);


			//GAME 1
			Salvo salvo1 = new Salvo(gamePlayer1, Arrays.asList("B5", "C5", "F1"),1);
			Salvo salvo2 = new Salvo(gamePlayer2, Arrays.asList("B4", "B5","B6"),1);
			Salvo salvo3 = new Salvo(gamePlayer1, Arrays.asList("F2","D5"),2);
			Salvo salvo4 = new Salvo(gamePlayer2, Arrays.asList("E1","H3","A2"),2);

			//GAME 2
			Salvo salvo5 = new Salvo(gamePlayer3, Arrays.asList("A2", "A4", "G6"),1);
			Salvo salvo6 = new Salvo(gamePlayer4, Arrays.asList("B5", "D5", "C7"),1);
			Salvo salvo7 = new Salvo(gamePlayer3, Arrays.asList("A3","H6"),2);
			Salvo salvo8 = new Salvo(gamePlayer4, Arrays.asList("C5","C6"),2);

			//GAME 3
			Salvo salvo9  = new Salvo(gamePlayer5, Arrays.asList("G6", "H6", "A4" ),1);
			Salvo salvo10 = new Salvo(gamePlayer6, Arrays.asList("H1", "H2", "H3"),1);
			Salvo salvo11 = new Salvo(gamePlayer5, Arrays.asList("A2", "A3", "D8"),2);
			Salvo salvo12 = new Salvo(gamePlayer6, Arrays.asList("E1", "F2", "G3"),2);

			//GAME 4
			Salvo salvo13  = new Salvo(gamePlayer7, Arrays.asList("A3", "A4", "F7" ),1);
			Salvo salvo14 = new Salvo(gamePlayer8, Arrays.asList("B5", "C6", "H1"),1);
			Salvo salvo15= new Salvo(gamePlayer7, Arrays.asList("A2", "G6", "H6"),2);
			Salvo salvo16 = new Salvo(gamePlayer8, Arrays.asList("C5", "C7", "D5"),2);

			//GAME 5
			Salvo salvo17 = new Salvo(gamePlayer9, Arrays.asList("A1", "A2", "A3" ),1);
			Salvo salvo18 = new Salvo(gamePlayer10, Arrays.asList("B5", "B6", "C7"),1);
			Salvo salvo19 = new Salvo(gamePlayer9, Arrays.asList("G6", "G7", "G8" ),2);
			Salvo salvo20 = new Salvo(gamePlayer10, Arrays.asList("C6", "D6", "E6"),2);
			Salvo salvo21 = new Salvo(gamePlayer10, Arrays.asList("H1", "H8"),3);



			Score score1= new Score(player1, game1, 1, 0);
			Score score2= new Score(player2, game1, 0, 0);
			Score score3= new Score(player1, game2, 0.5, 1);
			Score score4= new Score(player2, game2, 0.5, 1);
			Score score5= new Score(player2, game3, 1, 2);
			Score score6= new Score(player4, game3, 0, 2);
			Score score7= new Score(player2, game4, 0.5, 3);
			Score score8= new Score(player1, game4, 0.5, 3);


			PlayerRep.saveAll(Arrays.asList(player1,player2,player3,player4));
			GameRep.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6,game7,game8));
			GPRep.saveAll(Arrays.asList(gamePlayer1,gamePlayer2,gamePlayer3,gamePlayer4,gamePlayer5,gamePlayer6,gamePlayer7,
					gamePlayer8,gamePlayer9,gamePlayer10,gamePlayer11,gamePlayer12,gamePlayer13,gamePlayer14));
			ShipRep.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6,ship7,ship8,ship9,ship10,ship11,ship12,ship13,ship14,
					ship15,ship16,ship17,ship18,ship19,ship20,ship21,ship22,ship23,ship24,ship25,ship26,ship27));
			SalvoRep.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5,salvo6,salvo7,salvo8,salvo9,salvo10,
					salvo11,salvo12,salvo13,salvo14,salvo15,salvo16,salvo17,salvo18,salvo19,salvo20,salvo21));
			ScoreRep.saveAll(Arrays.asList(score1,score2,score3,score4,score5,score6,score7,score8));

		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRep;


	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRep.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}

}





@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

				.antMatchers("/api/players","/api/leaderboards","/api/login","/api/games","/web/*","/web/**").permitAll()
				.antMatchers("/api/**").hasAuthority("USER")
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.anyRequest().denyAll();


		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}


	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withDefaultPasswordEncoder()
						.username("user")
						.password("password")
						.roles("USER")
						.build();

		return new InMemoryUserDetailsManager(user);
	}
}

