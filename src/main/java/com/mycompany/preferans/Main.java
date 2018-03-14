package com.mycompany.preferans;

import com.mycompany.preferans.game_with_attributes.card_and_deck.Deck;
import com.mycompany.preferans.game_with_attributes.schemes.Leningrad;
import com.mycompany.preferans.game_with_attributes.schemes.Rostov;
import com.mycompany.preferans.game_with_attributes.schemes.Scheme;
import com.mycompany.preferans.game_with_attributes.schemes.Sochi;
import com.mycompany.preferans.subjects.Dealer;
import com.mycompany.preferans.subjects.Player;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final int NUMBER_OF_PLAYERS = 3;

    private static final Logger log = Logger.getLogger(Main.class);

    public static void play() {
        Set<Player> players = new HashSet<>();

        log.info("Choose scheme of game(Sochi/Rostov/Leningrad):");
        Scanner scanner = new Scanner(System.in);
        String schemeName = scanner.nextLine();

        Scheme scheme;

        scheme = getScheme(schemeName);
        if (scheme == null) return;

        log.info("You have chosen " + scheme.getClass().getSimpleName()+".");

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

        dealer.playGame(players, partiesToPlay);
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
