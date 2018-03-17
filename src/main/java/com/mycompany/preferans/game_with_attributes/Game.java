package com.mycompany.preferans.game_with_attributes;

import com.mycompany.preferans.subjects.Player;

import java.util.*;

public class Game {
    private static final int SCORES_FOR_POOL_POINTS = 10;
    private static final int SCORES_FOR_WHIST_POINTS = 1;
    private static final int SCORES_FOR_DUMP_POINTS = -10;

    private Map<Player, List<Scores>> playersAndScores;

    private Set<Player> players;

    private int partiesToPlay;
    private int playedParties;

    private List<Party> parties;

    public Game(int partiesToPlay, Set<Player> players) {
        this.players = players;

        parties = new ArrayList<>();

        this.partiesToPlay = partiesToPlay;

        playersAndScores = new HashMap<>();

        for (Player player : players) {

            List<Scores> scores = new ArrayList<>();

            playersAndScores.put(player, scores);
        }
    }

    public List<Party> getParties() {
        return parties;
    }

    public int getPoolPoints(Player player, int numberOfDealing) {
        return playersAndScores.get(player).subList(0, numberOfDealing).stream()
                .mapToInt(Scores::getPoolPoints)
                .sum();
    }

    public int getWhistPoints(Player player, Player playerOnWhistPointsToTake, int numberOfDealing) {
        return playersAndScores.get(player).subList(0,numberOfDealing).stream()
                .mapToInt(a -> a.getWhistPoints().get(playerOnWhistPointsToTake))
                .sum();
    }

    public int getDumpPoints(Player player,int numberOfDealing) {
        return playersAndScores.get(player).subList(0, numberOfDealing).stream()
                .mapToInt(Scores::getDumpPoints)
                .sum();
    }

    public int getAllPoints(Player player, int numberOfDealing) {
        int poolPoints = getPoolPoints(player, numberOfDealing);

        int whistPoints = 0;

        for (Player player1 : players) {
            if (player1 != player) {
                whistPoints += getWhistPoints(player, player1, numberOfDealing);
            }
        }

        int dumpPoints = getDumpPoints(player, numberOfDealing);

        return poolPoints * SCORES_FOR_POOL_POINTS +
                whistPoints * SCORES_FOR_WHIST_POINTS +
                dumpPoints * SCORES_FOR_DUMP_POINTS;
    }

    public int getPartiesToPlay() {
        return partiesToPlay;
    }

    public int getPlayedParties() {
        return playedParties;
    }

    public void setPlayedParties(int playedParties) {
        this.playedParties = playedParties;
    }

    public Map<Player, List<Scores>> getPlayersAndScores() {
        return playersAndScores;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void addParty(Party party) {
        parties.add(party);
    }
}
