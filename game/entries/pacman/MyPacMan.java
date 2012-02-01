package game.entries.pacman;

import java.util.LinkedList;

import ai.hsm.Action;
import ai.hsm.Condition;
import ai.hsm.SubMachineState;
import ai.hsm.Transition;
import game.controllers.PacManController;
import game.core.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan implements PacManController
{
	static final LinkedList<Action> NO_ACTION = new LinkedList<Action>();
	
	class PacManTransition extends Transition {
		String message;
		
		public PacManTransition(Condition condition) {
			this("", condition);
		}
		
		public PacManTransition(String message, Condition condition) {
			this.condition = condition;
			this.message = message;
		}
		
		@Override
		public LinkedList<Action> getAction() {
			//System.out.println(message);
			return NO_ACTION;
		}
	}
	
	class PacManState extends SubMachineState {
		public LinkedList<Action> action;
		public LinkedList<Action> entryAction = NO_ACTION;
		public LinkedList<Action> exitAction = NO_ACTION;
		
		public String message;
		
		public PacManState(PacManAction action, String message) {
			this.action = new LinkedList<Action>();
			this.action.add(action);
			this.message = message;
		}
		
		public PacManState(PacManAction action) {
			this(action, "");
		}
		
		@Override
		public LinkedList<Action> getAction() {
			return action;
		}
		
		@Override
		public LinkedList<Action> getEntryAction() {
			//System.out.println("\tEntering: " + message);
			return entryAction;
		}
		
		@Override
		public LinkedList<Action> getExitAction() {
			return exitAction;
		}
	}
	
	static final int CLOSE_DIST = 82;
	
	public static int[] ghostDist = new int[4];
	public static int closestGhost = -1;
	public static int closestBlueGhost = -1;
	public static int closestNonBlueGhost = -1;
	public static int currentLoc = -1;
	
	SubMachineState root;
	LinkedList<PacManAction> pacmanActions = new LinkedList<PacManAction>();
	LinkedList<Action> resultActions = new LinkedList<Action>();

	
	
	public MyPacMan() {
		root = new SubMachineState();
		
		// Build states
		PacManState eatPillState = new PacManState(new NearestPillAvoidPowerAction(), "eat pill");
		root.addState(eatPillState);
		root.initialState = eatPillState;
		
		SubMachineState handleGhostState = new SubMachineState();
		root.addState(handleGhostState);
		
		PacManState eatGhostState = new PacManState(new EatGhostAction(), "eat ghost");
		handleGhostState.addState(eatGhostState);
		
		PacManState runAwayState = new PacManState(new RunAwayAction(), "run away");
		handleGhostState.addState(runAwayState);
		handleGhostState.initialState = runAwayState;
		
		
		// Build Transitions
		PacManTransition close = new PacManTransition("close transition", new Condition() {

			@Override
			public boolean test() {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					
					// if ghost exists and is close 
					if (ghostDist[i] >= 0 && ghostDist[i] < CLOSE_DIST) {
						return true;
					}
				}
				
				// if all ghosts are not close
				return false;
			}
			
		});
		
		PacManTransition far = new PacManTransition("far transition", new Condition() {

			@Override
			public boolean test() {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					// if ghost exists and is close enough then can't be far
					if (ghostDist[i] >= 0 && ghostDist[i] < CLOSE_DIST) {
						return false;
					}
				}
				
				// all ghosts must be far
				return true;
			}
			
		});
		
		PacManTransition nearBlue = new PacManTransition("near blue", new Condition() {

			@Override
			public boolean test() {
				// if closest blue doesn't exist
				// then can be near blue
				if (closestBlueGhost < 0) {
					return false;
				} 
				
				// if closest non-blue doesn't exist
				// then we can't be near non blue
				else if (closestNonBlueGhost < 0) {
					return true;
				} 
				
				// return true if blue is closer than non-blue
				else {
					return ghostDist[closestBlueGhost] < ghostDist[closestNonBlueGhost];
				}
			}
			
		});
		
		PacManTransition nearNonBlue = new PacManTransition("near non-blue", new Condition() {

			@Override
			public boolean test() {
				// if nearest blue ghost doesn't exist
				// then can't be near blue
				if (closestBlueGhost < 0) {
					return true;
				} 
				
				// if nearest non blue ghost doesn't exist
				// then can't be near non-blue
				else if (closestNonBlueGhost < 0) {
					return false;
				} 
				
				// return true if non blue is closer
				else  {
					return ghostDist[closestBlueGhost] > ghostDist[closestNonBlueGhost];
				}
			}
			
		});
		
		// Join states with transitions
		bindTransition(eatPillState, handleGhostState, close);
		bindTransition(handleGhostState, eatPillState, far);
		
		bindTransition(runAwayState, eatGhostState, nearBlue);
		bindTransition(eatGhostState, runAwayState, nearNonBlue);
	}
	
	//Place your game logic here to play the game as Ms Pac-Man
	public int getAction(Game game, long timeDue)
	{		
		currentLoc = game.getCurPacManLoc();
		
		
		closestGhost = -1;
		closestBlueGhost = -1;
		closestNonBlueGhost = -1;
		int dist = Integer.MAX_VALUE;
		int distBlue = Integer.MAX_VALUE;
		int distNonBlue = Integer.MAX_VALUE;
		
		for (int i = 0; i < Game.NUM_GHOSTS; i++) {
			ghostDist[i] = game.getPathDistance(currentLoc, game.getCurGhostLoc(i));
			
			// if the ghost is not in the cell
			if (ghostDist[i] >= 0) { 
				
				// find closest ghost
				if (ghostDist[i] < dist) {
					dist = ghostDist[i];
					closestGhost = i;
				}
				
				// find closest blue ghost
				if (game.isEdible(i) && ghostDist[i] < distBlue) {
					distBlue = ghostDist[i];
					closestBlueGhost = i;
				}
				
				// find closest non-blue ghost
				if (!game.isEdible(i) && ghostDist[i] < distNonBlue) {
					distNonBlue = ghostDist[i];
					closestNonBlueGhost = i;
				}
			}
		}
		
		while (resultActions.isEmpty()) {
			resultActions.addAll(root.update().actions);
		}
		
		
		for (Action action : resultActions) {
			pacmanActions.add((PacManAction)action);
		}
		
		// reset result actions
		resultActions.clear();
		
		// preform PacMan action
		return pacmanActions.removeFirst().act(game);
	}
	
	static void bindTransition(SubMachineState src, SubMachineState target, Transition transition) {
		src.addTransition(transition);
		transition.source = src;
		transition.target = target;
	}
}