package com.mycompany.preferans.game_with_attributes.schemes;

import com.mycompany.preferans.game_with_attributes.Game;
import com.mycompany.preferans.game_with_attributes.Party;
import com.mycompany.preferans.game_with_attributes.Scores;
import com.mycompany.preferans.subjects.Player;

import java.util.Map;

public interface Scheme {
    int MIN_NUMBER_OF_TRICKS = 6;
    int SCORES_FOR_PLAY_NOTHING = 10;

    void changeScores(Game game, Party party, Map<Player,Scores> scores);
}
