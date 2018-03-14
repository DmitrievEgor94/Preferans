package com.mycompany.preferans.game_with_attributes;

import com.mycompany.preferans.subjects.Player;

import java.util.*;

public class Game {
    private Map<Player, List<Scores>> playersAndScores;

    private Set<Player> players;

    private int partiesToPlay;
    private int playedParties;

    private List<Party> parties;

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

    public int getPartiesToPlay() {
        return partiesToPlay;
    }

    public int getPlayedParties() {
        return playedParties;
    }

    public Map<Player, List<Scores>> getPlayersAndScores() {
        return playersAndScores;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void addParty(Party party){
        parties.add(party);
    }

    public void setPlayedParties(int playedParties){
        this.playedParties = playedParties;
    }
}
