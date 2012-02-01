package game.entries.pacman;

import ai.hsm.Action;
import game.core.Game;

abstract class PacManAction extends Action {		
	abstract int act(Game game);
}
