package game.entries.pacman;

import game.core.Game;

public class RunAwayAction extends PacManAction {

	@Override
	int act(Game game) {
		int closest = -1;
		int dist = Integer.MAX_VALUE;
		for (int i = 0; i < Game.NUM_GHOSTS; i++) {
			int ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
			
			if (ghostDist < dist) {
				dist = ghostDist;
				closest = i;
			}
		}
		
		return game.getNextPacManDir(closest, false, Game.DM.PATH);
	}

}
