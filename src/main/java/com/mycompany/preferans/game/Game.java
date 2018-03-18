package com.mycompany.preferans.game;

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
        return playersAndScores.get(player).subList(0, numberOfDealing).stream()
                .mapToInt(a -> a.getWhistPoints().get(playerOnWhistPointsToTake))
                .sum();
    }

    public int getDumpPoints(Player player, int numberOfDealing) {
        return playersAndScores.get(player).subList(0, numberOfDealing).stream()
                .mapToInt(Scores::getDumpPoints)
                .sum();
    }

    public int getAllPoints(Player playerPointsToCount, int numberOfDealing) {
        Map<Player, Integer> allPoolPoints = new HashMap<>();
        Map<Player, Integer> allDumpPoints = new HashMap<>();

        Map<Player, Map<Player, Integer>> allWhistPoints = new HashMap<>();

        for (Player player : players) {
            allPoolPoints.put(player, getPoolPoints(player, numberOfDealing));
            allDumpPoints.put(player, getDumpPoints(player, numberOfDealing));

            Map<Player, Integer> whistPoints = new HashMap<>();

            for (Player player1 : players) {
                if (player1 != player) {
                    whistPoints.put(player1, getWhistPoints(player, player1, numberOfDealing));
                }
            }

            allWhistPoints.put(player, whistPoints);
        }

        equalizationPoolAndDumpPoints(allPoolPoints, allDumpPoints);

        equalizationWhistPoints(allWhistPoints);

        int whistPointsForPlayerToCount = 0;

        for (Player player : players) {
            if (player != playerPointsToCount) {
                whistPointsForPlayerToCount += allWhistPoints.get(playerPointsToCount).get(player);

            }
        }

        return whistPointsForPlayerToCount + allDumpPoints.get(playerPointsToCount);
    }

    private void equalizationWhistPoints(Map<Player, Map<Player, Integer>> allWhistPoints) {
        for (Player player : players) {
            for (Player player1 : players) {
                if (player != player1) {
                    int newWhistPoints = allWhistPoints.get(player).get(player1)
                            - allWhistPoints.get(player1).get(player);

                    allWhistPoints.get(player).put(player1, newWhistPoints);
                    allWhistPoints.get(player1).put(player, -newWhistPoints);
                }
            }
        }
    }

    private void equalizationPoolAndDumpPoints(Map<Player, Integer> allPoolPoints, Map<Player, Integer> allDumpPoints) {
        Integer maxPoolPoints = allPoolPoints.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getValue();

        for (Player player : players) {
            int difNumberOfPoints = maxPoolPoints - allPoolPoints.get(player);

            int newDumpPoints = difNumberOfPoints + allDumpPoints.get(player);

            allDumpPoints.put(player, newDumpPoints);
        }

        Integer minDumpPoints = allDumpPoints.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getValue();

        allDumpPoints.entrySet().forEach(a -> a.setValue(a.getValue() - minDumpPoints));

        int meanNumberOfDumpPoints = allDumpPoints.entrySet().stream()
                .mapToInt(Map.Entry::getValue)
                .sum() * 10 / 3;

        allDumpPoints.entrySet().forEach(a -> a.setValue(meanNumberOfDumpPoints - 10 * a.getValue()));
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
