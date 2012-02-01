package game.entries.pacman;

import game.core.Game;

public class RunAwayAction extends PacManAction {
	
	static final int SHORTEST_PATH = 10;

	@Override
	int act(Game game) {
		int current = game.getCurPacManLoc();
		
		int[] targets = game.getPowerPillIndices();
		int[][] paths = new int[4][];
		int[] ghosts = new int[4];
		int[] ghostDists = new int[4];
		
		// fill paths
		for (int i = 0; i < targets.length; i++) {
			paths[i] = game.getPath(current, targets[i]);
		}
		
		// fill ghost location array
		for (int i = 0; i < ghosts.length; i++) {
			ghosts[i] = game.getCurGhostLoc(i);
			ghostDists[i] = game.getPathDistance(current, ghosts[i]);
		}
		
		boolean skipPath = false;
		boolean foundPath = false;
		int targetIndex = -1;
		for (int path = 0; (path < paths.length) && !foundPath; path++) {
			
			// skip the path if we are too close
			if (paths[path].length < SHORTEST_PATH) skipPath = true;
			
			// for each step of path
			for (int step = 0; (step < paths[path].length) && !skipPath; step++) {
				
				//compare against each ghost
				for (int ghost = 0; ghost < ghosts.length; ghost++) {
					
					// ghost is in range
					if (ghostDists[ghost] >= 0 && ghostDists[ghost] < MyPacMan.CLOSE_DIST) {
						
						// ghost is in path
						if (ghosts[ghost] == paths[path][step]) {
							skipPath = true;
							break;
						} 
					}
					
				}
			}
			
			// if there was no collision
			if (!skipPath) {
				targetIndex = path;
				foundPath = true;
			}
			
			skipPath = false;
		}
		
		if (foundPath) {
			System.out.println("Found a way out");
			return game.getNextPacManDir(targets[targetIndex], true, Game.DM.PATH);
		} else {
			System.out.println("Just running away from closest");
			return game.getNextPacManDir(game.getCurGhostLoc(MyPacMan.closestGhost), false, Game.DM.PATH);
		}
	}

}
