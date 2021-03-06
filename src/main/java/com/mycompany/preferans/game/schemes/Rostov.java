package com.mycompany.preferans.game.schemes;

import com.mycompany.preferans.game.Game;
import com.mycompany.preferans.game.Party;
import com.mycompany.preferans.game.Scores;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public class Rostov implements Scheme {
    @Override
    public void changeScores(Game game, Party party, Map<Player, Scores> playersScoresMap) {
        writeScores(game, party, playersScoresMap, 1, 2, 1);
    }
}
