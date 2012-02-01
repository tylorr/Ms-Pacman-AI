package game.entries.pacman;

import java.awt.Color;

import game.core.Game;
import game.core.GameView;

public class EatPowerPillAction extends PacManAction {

	@Override
	int act(Game game) {
		int dir = game.getNextPacManDir(MyPacMan.nearestPowerPillLoc, true, Game.DM.PATH);
		GameView.addPoints(game, Color.YELLOW, game.getPath(game.getCurPacManLoc(), MyPacMan.nearestPowerPillLoc));
		return dir;
	}

}
