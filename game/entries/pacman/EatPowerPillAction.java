package game.entries.pacman;

import game.core.Game;

public class EatPowerPillAction extends PacManAction {

	@Override
	int act(Game game) {
		int dir = game.getNextPacManDir(MyPacMan.nearestPowerPillLoc, true, Game.DM.PATH);
		return dir;
	}

}
