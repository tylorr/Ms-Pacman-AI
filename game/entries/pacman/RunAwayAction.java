package game.entries.pacman;

import java.awt.Color;
import java.util.Random;

import game.core.Game;
import game.core.GameView;

public class RunAwayAction extends PacManAction {
	
	Random random = new Random();

	@Override
	int act(Game game) {
		int[] possibleDirs = game.getPossiblePacManDirs(true);
		
		int[] preferredDirs = new int[possibleDirs.length - 1];
		
		int avoidDir = game.getNextPacManDir(game.getCurGhostLoc(MyPacMan.closestGhost), true, Game.DM.PATH);
		
		int index = 0;
		for (int i = 0; i < possibleDirs.length; i++) {
			if (possibleDirs[i] != avoidDir) {
				preferredDirs[index] = possibleDirs[i];
				index++;
			}
		}
		
		int dirIndex = random.nextInt(preferredDirs.length);
		
		GameView.addPoints(game, Color.CYAN, game.getPath(game.getCurPacManLoc(), game.getCurGhostLoc(MyPacMan.closestGhost)));
		
		return preferredDirs[dirIndex];
		/*
		int closest = -1;
		int dist = Integer.MAX_VALUE;
		int ghostDist;
		for (int i = 0; i < Game.NUM_GHOSTS; i++) {
			ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
			
			if (ghostDist >= 0 && ghostDist < dist) {
				dist = ghostDist;
				closest = i;
			}
		}
		
		GameView.addPoints(game, Color.CYAN, game.getPath(game.getCurPacManLoc(), game.getCurGhostLoc(closest)));
		
		return game.getNextPacManDir(game.getCurGhostLoc(closest), false, Game.DM.PATH);
		*/
	}

}
