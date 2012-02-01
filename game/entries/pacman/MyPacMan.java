package game.entries.pacman;

import java.util.LinkedList;

import ai.hsm.Action;
import ai.hsm.Condition;
import ai.hsm.SubMachineState;
import ai.hsm.Transition;
import game.Exec;
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
	
	abstract class PacManCondition implements Condition {

		@Override
		public boolean test() {
			return false;
		}
		
		public abstract boolean test(Game game);
	}
	
	class PacManTransition extends Transition {
		Exec exec;
		PacManCondition condition;
		String message;
		
		public PacManTransition(Exec exec, PacManCondition condition) {
			this(exec, "", condition);
		}
		
		public PacManTransition(Exec exec, String message, PacManCondition condition) {
			this.exec = exec;
			this.condition = condition;
			this.message = message;
		}
		
		@Override
		public LinkedList<Action> getAction() {
			System.out.println(message);
			return NO_ACTION;
		}
		
		@Override
		public boolean isTriggered() {
			return condition.test(exec.game);
		}
	}
	
	class PacManState extends SubMachineState {
		public PacManAction action;
		public String message;
		
		public PacManState(PacManAction action, String message) {
			this.action = action;
			this.message = message;
		}
		
		public PacManState(PacManAction action) {
			this(action, "");
		}
		
		@Override
		public LinkedList<Action> getAction() {
			
			LinkedList<Action> result = new LinkedList<Action>();
			result.add(this.action);
			return result;
		}
		
		@Override
		public LinkedList<Action> getEntryAction() {
			System.out.println("\tEntering: " + message);
			return NO_ACTION;
		}
		
		@Override
		public LinkedList<Action> getExitAction() {
			return NO_ACTION;
		}
	}
	
	static final int CLOSE_DIST = 25;
	
	public static int[] ghostDist = new int[4];
	public static int closestGhost = -1;
	public static int closestBlueGhost = -1;
	public static int currentLoc = -1;
	
	SubMachineState root;
	LinkedList<PacManAction> pacmanActions = new LinkedList<PacManAction>();
	LinkedList<Action> resultActions = new LinkedList<Action>();

	public static int closestNonBlueGhost;
	
	public MyPacMan(Exec exec) {
		root = new SubMachineState();
		
		// Build states
		PacManState eatPillState = new PacManState(new NearestPillAction(), "eat pill");
		root.addState(eatPillState);
		root.initialState = root.currentState = eatPillState;
		
		SubMachineState handleGhostState = new SubMachineState();
		root.addState(handleGhostState);
		
		PacManState eatGhostState = new PacManState(new NearestPillAction(), "eat ghost"); // TODO: change to eat ghost action
		handleGhostState.addState(eatGhostState);
		
		SubMachineState avoidGhostState = new SubMachineState();
		handleGhostState.addState(avoidGhostState);
		handleGhostState.initialState = handleGhostState.currentState = avoidGhostState;
		
		PacManState runAwayState = new PacManState(new RunAwayAction(), "run away");
		avoidGhostState.addState(runAwayState);
		avoidGhostState.initialState = avoidGhostState.currentState = runAwayState;
		
		PacManState eatPowerPillState = new PacManState(new NearestPillAction(), "eat power pill"); // TODO: change to eat power pill action
		avoidGhostState.addState(eatPowerPillState);
		
		
		// Build Transitions
		PacManTransition close = new PacManTransition(exec, "close transition", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					if (ghostDist[i] >= 0 && ghostDist[i] < CLOSE_DIST) {
						return true;
					}
				}
				return false;
			}
			
		});
		
		PacManTransition far = new PacManTransition(exec, "far transition", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					if (ghostDist[i] >= 0 && ghostDist[i] < CLOSE_DIST) {
						return false;
					}
				}
				return true;
			}
			
		});
		
		PacManTransition nearBlue = new PacManTransition(exec, "near blue", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				if (closestBlueGhost < 0) {
					return false;
				} else if (closestNonBlueGhost < 0) {
					return true;
				} else {
					return ghostDist[closestBlueGhost] < ghostDist[closestNonBlueGhost];
				}
				/*
				int closest = -1;
				int dist = Integer.MAX_VALUE;
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					int ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
					
					if (ghostDist >= 0 && ghostDist < dist) {
						dist = ghostDist;
						closest = i;
					}
				}
				
				if (closest >= 0) {
					return game.isEdible(closest);
				}
				return false;
				*/
			}
			
		});
		
		PacManTransition nearNonBlue = new PacManTransition(exec, "near non-blue", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				if (closestBlueGhost < 0) {
					return true;
				} else if (closestNonBlueGhost < 0) {
					return false;
				} else  {
					return ghostDist[closestBlueGhost] > ghostDist[closestNonBlueGhost];
				}
				/*
				int closest = -1;
				int dist = Integer.MAX_VALUE;
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					int ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
					
					if (ghostDist >= 0 && ghostDist < dist) {
						dist = ghostDist;
						closest = i;
					}
				}
				
				if (closest >= 0) {
					return !game.isEdible(closest);
				}
				return false;
				*/
			}
			
		});
		
		PacManTransition nearPower = new PacManTransition(exec, "near power pill", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				int[] powerPills = game.getPowerPillIndicesActive();
				
				for (int i = 0; i < powerPills.length; i++) {
					int dist = game.getPathDistance(powerPills[i], game.getCurPacManLoc());
					
					if (dist >= 0 && dist < CLOSE_DIST) {
						return true;
					}
				}
				
				return false;
			}
			
		});
		
		PacManTransition farPower = new PacManTransition(exec, "far from power pill", new PacManCondition() {

			@Override
			public boolean test(Game game) {
				int[] powerPills = game.getPowerPillIndicesActive();
				
				for (int i = 0; i < powerPills.length; i++) {
					int dist = game.getPathDistance(powerPills[i], game.getCurPacManLoc());
					
					if (dist >= 0 && dist < CLOSE_DIST) {
						return false;
					}
				}
				
				return true;
			}
			
		});
		
		// Join states with transitions
		bindTransition(eatPillState, handleGhostState, close);
		bindTransition(handleGhostState, eatPillState, far);
		
		bindTransition(avoidGhostState, eatGhostState, nearBlue);
		bindTransition(eatGhostState, avoidGhostState, nearNonBlue);
		
		bindTransition(runAwayState, eatPowerPillState, nearPower);
		bindTransition(eatPowerPillState, runAwayState, farPower);
		
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
			
			if (ghostDist[i] >= 0) { 
				if (ghostDist[i] < dist) {
					dist = ghostDist[i];
					closestGhost = i;
				}
				
				if (game.isEdible(i) && ghostDist[i] < distBlue) {
					distBlue = ghostDist[i];
					closestBlueGhost = i;
				}
				
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
		
		resultActions.clear();
				
		return pacmanActions.removeFirst().act(game);
	}
	
	static void bindTransition(SubMachineState src, SubMachineState target, Transition transition) {
		src.addTransition(transition);
		transition.source = src;
		transition.target = target;
	}
}