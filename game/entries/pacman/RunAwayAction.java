package game.entries.pacman;

import java.awt.Color;
import java.util.LinkedList;

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
								if (ghosts[ghost] == paths[path][step]) {
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
				setTarget(game.getNextPacManDir(target, true, Game.DM.PATH));
				return true;
			}
			return false;
		}
		
	}
	
	class RunForJunction extends Task {

		@Override
		public boolean run() {
			
			int current = game.getCurPacManLoc();
			int[] neighbour = game.getPacManNeighbours();
			int[] junction = game.getJunctionIndices();
			
			boolean isJunction = false;
			isJunction = game.isJunction(current);
			
			for (int i = 0; i < neighbour.length && !isJunction; i++) {
				if (neighbour[i] != -1) {
					isJunction = game.isJunction(neighbour[i]);
				}
			}
			
			if (isJunction) {
				visitedJunctions.add(game.getTarget(current, junction, true, Game.DM.PATH));
			}
			
			
			int dist;
			int[] path;
			
			boolean foundJunction = false;
			boolean skipPath = false;
			int targetJunction = -1;
			
			for (int i = 0; i < junction.length && !foundJunction; i++) {
				dist = game.getPathDistance(current, junction[i]);
				path = game.getPath(current, junction[i]);
				
				if (dist < MAX_PATH_LENGTH && !visitedJunctions.contains(junction[i])) {
					for (int step = 0; step < path.length && !skipPath; step++) {
						for (int ghost = 0; ghost < Game.NUM_GHOSTS; ghost++) {
							if (path[step] == game.getCurGhostLoc(ghost)) {
								
								skipPath = true;
								break;
							}
						}
					}
					
					if (!skipPath) {
						foundJunction = true;
						targetJunction = junction[i];
					}
				}
			}
			
			if (foundJunction) {
				GameView.addPoints(game, Color.LIGHT_GRAY, game.getPath(current, targetJunction));
				setTarget(game.getNextPacManDir(targetJunction, true, Game.DM.PATH));
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
			
			setTarget(game.getNextPacManDir(game.getTarget(current, ghostLocations, true, Game.DM.PATH), false, Game.DM.PATH));
			visitedJunctions.clear();
			return true;
		}
		
	}
	
	public static final int MAX_PATH_LENGTH = 40;
	
	public static LinkedList<Integer> visitedJunctions = new LinkedList<Integer>();
	
	private int target = -1;
	Task taskTree;
	Game game;
	
	public RunAwayAction() {
		taskTree = new Selector();
		
		taskTree.children.add(new RunForPowerPill());
		taskTree.children.add(new RunForPill());
		taskTree.children.add(new RunForJunction());
		// TODO: add run for junction 
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
