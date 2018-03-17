package com.mycompany.preferans.game_with_attributes.schemes;

import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public class Leningrad implements Scheme {
    @Override
    public void changeScores(Game game, Party party, Map<Player, Scores> playersScoresMap) {
        writeScores(game, party, playersScoresMap, 2, 2, 2);
    }
}
