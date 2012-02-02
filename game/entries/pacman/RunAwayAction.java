package game.entries.pacman;

import java.awt.Color;
import java.util.ArrayList;

import ai.behaviourtree.Selector;
import ai.behaviourtree.Task;

import game.core.Game;
import game.core.GameView;

public class RunAwayAction extends PacManAction {
	
	class RunForPowerPill extends Task {

		@Override
		public boolean run() {
			
			int current = game.getCurPacManLoc();
			
			int[] targets = game.getPowerPillIndicesActive();
			
			// a power pill exists
			if (targets.length > 0) {
				
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
				int targetIndex = -1;
				
				int dist = Integer.MAX_VALUE;
				
				// loop over all paths to active power pills until a path has been found
				for (int path = 0; (path < paths.length) && paths[path] != null; path++) {
									
					// for each step of path
					for (int step = 0; (step < paths[path].length) && !skipPath; step++) {
						
						//compare against each ghost
						for (int ghost = 0; ghost < ghosts.length; ghost++) {
							
							// ghost is in range
							if (ghostDists[ghost] >= 0 && ghostDists[ghost] < MyPacMan.CLOSE_DIST) {
								
								// ghost is in path
								//int pathDist = game.getPathDistance(paths[path][step], ghosts[ghost]);
								int myDist = game.getPathDistance(current, paths[path][step]);
								int ghostDist = game.getGhostPathDistance(ghost, paths[path][step]);
								if (ghostDist > 0 && ghostDist < MyPacMan.POWER_DIST && (ghostDist < myDist + 1)) {
								//if (pathDist > 0 && pathDist < MyPacMan.POWER_DIST) {
									skipPath = true;
									break;
								} 
							}
							
						}
					}
					
					// if there was no ghost on the path
					// and it is the shortest path
					if (!skipPath && paths[path].length < dist) {
						dist = paths[path].length;
						targetIndex = path;
					}
					
					skipPath = false;
				}
				
				if (targetIndex != -1) {
					//System.out.println("Found a way out");
					GameView.addPoints(game, Color.GREEN, game.getPath(current, targets[targetIndex]));
					//return game.getNextPacManDir(targets[targetIndex], true, Game.DM.PATH);
					setTarget(game.getNextPacManDir(targets[targetIndex], true, Game.DM.PATH));
					
					visitedJunctions.clear();
					return true;
				}
			}
			return false;
		}
		
	}
	
	class RunForPill extends Task {
		@Override
		public boolean run() {
			int current = game.getCurPacManLoc();
			int[] activePills = game.getPillIndicesActive();
			int target = game.getTarget(current, activePills, true, Game.DM.PATH);
			int[] path = game.getPath(current, target);
			
			boolean ghostExists = false;
			
			// find path to nearest pill and check if ghost is on it
			for (int step = 0; (step < path.length) && !ghostExists; step++) {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					if (path[step] == game.getCurGhostLoc(i)) {
						ghostExists = true;
						break;
					}
				}
			}
			
			// if no ghost in the way
			if (!ghostExists) {
				visitedJunctions.clear();
				
				GameView.addPoints(game, Color.MAGENTA, game.getPath(current, target));
				
				setTarget(game.getNextPacManDir(target, true, Game.DM.PATH));
				return true;
			}
			return false;
		}
		
	}
	
	class RunForJunction extends Task {

		@Override
		public boolean run() {
			int i;
			int j = -1;
			int k;
			int[] path;
			int step;
			
			int current = game.getCurPacManLoc();
			
			if (game.isJunction(current)) {
				visitedJunctions.add(current);
			}
			
			int[] junction = game.getJunctionIndices();
			int[] array;
			ArrayList<Integer> list = new ArrayList<Integer>();
			
			for (i = 0; i < junction.length; i++) {
				list.add(junction[i]);
			}
			
			list.removeAll(visitedJunctions);
			
			boolean foundPath = false;
			boolean foundGhost = false;
			
			while (!foundPath && !list.isEmpty()) {
				array = new int[list.size()];
				
				for (i = 0; i < array.length; i++) {
					array[i] = list.get(i);
				}
				
				j = game.getTarget(current, array, true, Game.DM.PATH);
				path = game.getPath(current, j);
				
				foundGhost = false;
				for (step = 0; step < path.length && !foundGhost; step++) {
					for (k = 0; k < Game.NUM_GHOSTS; k++) {
						int ghostDist = game.getGhostPathDistance(k, path[step]);
						int myDist = game.getPathDistance(current, path[step]);
						
						//if (distance > 0 && distance < MyPacMan.JUNC_DIST) {
						if (ghostDist > 0 && ghostDist < MyPacMan.JUNC_DIST && (ghostDist < myDist)) {
							foundGhost = true;
							list.remove(new Integer(j));
							break;
						}
					}
				}
				
				
				
				foundPath = !foundGhost;
			}
			
			if (foundPath) {
				GameView.addPoints(game, Color.LIGHT_GRAY, game.getPath(current, j));
				setTarget(game.getNextPacManDir(j, true, Game.DM.PATH));
				return true;
			}
			
			return false;
		}
		
	}
	
	class NaiveRunAway extends Task {

		@Override
		public boolean run() {
			int current = game.getCurPacManLoc();
			int[] ghostLocations = new int[Game.NUM_GHOSTS];
			
			for (int i = 0; i < Game.NUM_GHOSTS; i++) {
				ghostLocations[i] = game.getCurGhostLoc(i);
			}
			
			int target = game.getTarget(current, ghostLocations, true, Game.DM.PATH);
			//GameView.addPoints(game, Color.RED, game.getPath(current, target));
			setTarget(game.getNextPacManDir(target, false, Game.DM.PATH));
			visitedJunctions.clear();
			return true;
		}
		
	}
	
	public static ArrayList<Integer> visitedJunctions = new ArrayList<Integer>();
	
	private int target = -1;
	Task taskTree;
	Game game;
	
	public RunAwayAction() {
		
		taskTree = new Selector();
		
		taskTree.children.add(new RunForPowerPill());
		//taskTree.children.add(new RunForPill());
		taskTree.children.add(new RunForJunction());
		taskTree.children.add(new NaiveRunAway());
	}
	
	public void setTarget(int target) {
		this.target = target;
	}

	@Override
	int act(Game game) {
		this.game = game;
		this.target = -1;
		
		if (!taskTree.run()) {
			throw new RuntimeException("No Selection");
		}
		
		return target;
	}

}
