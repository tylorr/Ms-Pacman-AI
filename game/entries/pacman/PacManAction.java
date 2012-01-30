package game.entries.pacman;

import game.core.Game;
import ai.fsm.Action;

abstract class PacManAction extends Action {		
	abstract int act(Game game);
}