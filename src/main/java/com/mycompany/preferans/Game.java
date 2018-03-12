package com.mycompany.preferans;

import com.mycompany.preferans.subjects.Player;

import java.util.*;

public class Game {
    private Map<Player, List<Scores>> playersAndScores;

    private Set<Player> players;

    private int partiesToPlay;
    private int playedParties;

    List<Party> parties;

    public Game(int partiesToPlay, Set<Player> players) {
        this.players = players;

        parties =  new ArrayList<>();

        this.partiesToPlay = partiesToPlay;

        playersAndScores = new HashMap<>();

        for (Player player : players) {

            List<Scores> scores = new ArrayList<>();

            playersAndScores.put(player, scores);
        }
    }
}
