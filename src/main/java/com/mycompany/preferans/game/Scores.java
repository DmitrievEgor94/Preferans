package com.mycompany.preferans.game;

import com.mycompany.preferans.subjects.Player;

import java.util.HashMap;
import java.util.Map;

public class Scores {
    private int poolPoints;
    private Map<Player, Integer> whistPoints;
    private int dumpPoints;

    public Scores() {
        whistPoints = new HashMap<>();
    }

    int getPoolPoints() {
        return poolPoints;
    }

    public void setPoolPoints(int poolPoints) {
        this.poolPoints = poolPoints;
    }

    public Map<Player, Integer> getWhistPoints() {
        return whistPoints;
    }

    int getDumpPoints() {
        return dumpPoints;
    }

    public void setDumpPoints(int dumpPoints) {
        this.dumpPoints = dumpPoints;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("      pool points: ").append(poolPoints).append("\n");

        for (Map.Entry<Player, Integer> playerAndWhists : whistPoints.entrySet()) {
            s.append("      whists on ").append(playerAndWhists.getKey()).append(" ")
                    .append(playerAndWhists.getValue()).append("\n");
        }

        s.append("      dump points: ").append(dumpPoints);

        return s.toString();
    }
}
