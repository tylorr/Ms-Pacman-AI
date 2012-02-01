package game.entries.pacman;

import game.core.Game;

public class EatGhostAction extends PacManAction {
	@Override
	int act(Game game) {
		int dir = game.getNextPacManDir(game.getCurGhostLoc(MyPacMan.closestBlueGhost), true, Game.DM.PATH);
		return dir;
	}

}
