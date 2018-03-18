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

        classWithApiFunctions.api1(2, "api1_2.txt", false);
        classWithApiFunctions.api1(5, "api1_5.txt", false);
        classWithApiFunctions.api2(4, "api2_4.txt", false);
        classWithApiFunctions.api3(4, "api3_3.txt", false);
        classWithApiFunctions.api4(7, "api4_7.txt", false);
        classWithApiFunctions.api5(6, "api5_6.txt", false);
        classWithApiFunctions.api7(8, "api7_8.txt", listOfPlayers.get(0), false);
        classWithApiFunctions.api8(10, "api8_10.txt", listOfPlayers.get(1), false);
        classWithApiFunctions.api9(10, "api9_10.txt", listOfPlayers.get(2), false);
        classWithApiFunctions.api10(12, "api10.txt", false);

        for (int i = 1; i <= 12; i++) {
            String nameOfFile = "api6_" + i + ".txt";
            classWithApiFunctions.api6(i, nameOfFile, false);
        }
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
