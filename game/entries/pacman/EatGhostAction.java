package game.entries.pacman;

import java.awt.Color;

import game.core.Game;
import game.core.GameView;

public class EatGhostAction extends PacManAction {
	@Override
	int act(Game game) {
		int current = game.getCurPacManLoc();
		int target = game.getCurGhostLoc(MyPacMan.closestBlueGhost);
		GameView.addPoints(game, Color.ORANGE, game.getPath(current, target));
		int dir = game.getNextPacManDir(target, true, Game.DM.PATH);
		return dir;
	}
}
