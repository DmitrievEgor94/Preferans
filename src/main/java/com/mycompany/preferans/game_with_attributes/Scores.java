package com.mycompany.preferans.game_with_attributes;

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

    public int getPoolPoints() {
        return poolPoints;
    }

    public void setPoolPoints(int poolPoints) {
        this.poolPoints = poolPoints;
    }

    public Map<Player, Integer> getWhistPoints() {
        return whistPoints;
    }

    public int getDumpPoints() {
        return dumpPoints;
    }

    public void setDumpPoints(int dumpPoints) {
        this.dumpPoints = dumpPoints;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("      pool points: ").append(poolPoints).append("\n");

        for (Player player : whistPoints.keySet()) {
            s.append("      whists on ").append(player).append(" ").append(whistPoints.get(player)).append("\n");
        }

        s.append("      dump points: ").append(dumpPoints);

        return s.toString();
    }
}
