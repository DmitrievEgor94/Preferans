package com.mycompany.preferans;

import com.mycompany.preferans.api.ClassWithApiFunctions;
import com.mycompany.preferans.game.Game;
import com.mycompany.preferans.game.deck.Deck;
import com.mycompany.preferans.game.schemes.Leningrad;
import com.mycompany.preferans.game.schemes.Rostov;
import com.mycompany.preferans.game.schemes.Scheme;
import com.mycompany.preferans.game.schemes.Sochi;
import com.mycompany.preferans.subjects.Dealer;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.util.*;

public class Main {
    private static final int NUMBER_OF_PLAYERS = 3;

    private static final Logger log = Logger.getLogger(Main.class);

    private static void play() {
        Set<Player> players = new HashSet<>();

        log.info("Choose scheme of game(Sochi/Rostov/Leningrad):");
        Scanner scanner = new Scanner(System.in);
        String schemeName = scanner.nextLine();

        Scheme scheme;

        scheme = getScheme(schemeName);
        if (scheme == null) return;

        log.info("You have chosen " + scheme.getClass().getSimpleName() + ".");

        log.info("Choose number of parties to play:");
        int partiesToPlay = scanner.nextInt();

        log.info("You have chosen to play " + partiesToPlay + " parties.");

        for (int i = 1; i <= NUMBER_OF_PLAYERS; i++) {
            Player player = new Player("Player" + i);
            players.add(player);

            log.info(player + " is " + "playing!");
        }

        Deck deck = new Deck();

        Dealer dealer = new Dealer(scheme, deck);

        Game game = dealer.playGame(players, partiesToPlay);

        ClassWithApiFunctions classWithApiFunctions = new ClassWithApiFunctions(game);

        List<Player> listOfPlayers = new ArrayList<>(players);

        classWithApiFunctions.api1(1, "api1.txt", false);
        classWithApiFunctions.api2(1, "api2.txt", false);
        classWithApiFunctions.api3(1, "api3.txt", false);
        classWithApiFunctions.api4(1, "api4.txt", false);
        classWithApiFunctions.api5(1, "api5.txt", false);
        classWithApiFunctions.api6(1, "api6.txt", false);
        classWithApiFunctions.api7(1, "api7.txt", listOfPlayers.get(0), false);
        classWithApiFunctions.api9(100, "api9.txt", listOfPlayers.get(0), false);
        classWithApiFunctions.api10(10, "api10.txt", false);
    }

    private static Scheme getScheme(String schemeName) {
        Scheme scheme;
        if ("Sochi".equalsIgnoreCase(schemeName)) {
            scheme = new Sochi();
        } else if ("Rostov".equalsIgnoreCase(schemeName)) {
            scheme = new Rostov();
        } else if ("Leningrad".equalsIgnoreCase(schemeName)) {
            scheme = new Leningrad();
        } else {
            log.error("You chose wrong type of game!");
            return null;
        }
        return scheme;
    }

    public static void main(String[] args) {
        play();
    }
}
