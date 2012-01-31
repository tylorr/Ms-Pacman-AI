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
	
	abstract class PacManCondition implements Condition {

		@Override
		public boolean test() {
			return false;
		}
		
		public abstract boolean test(Game game);
	}
	
	class PacManTransition extends Transition {
		Game game;
		PacManCondition condition;
		
		public PacManTransition(Game game, PacManCondition condition) {
			this.game = game;
			this.condition = condition;
		}
		
		@Override
		public LinkedList<Action> getAction() {
			return NO_ACTION;
		}
		
		@Override
		public boolean isTriggered() {
			return condition.test(game);
		}
	}
	
	class PacManState extends SubMachineState {
		public PacManAction action;
		
		public PacManState(PacManAction action) {
			this.action = action;
		}
		
		@Override
		public LinkedList<Action> getAction() {
			LinkedList<Action> result = new LinkedList<Action>();
			result.add(this.action);
			return result;
		}
		
		@Override
		public LinkedList<Action> getEntryAction() {
			return NO_ACTION;
		}
		
		@Override
		public LinkedList<Action> getExitAction() {
			return NO_ACTION;
		}
	}
	
	static final int CLOSE_DIST = 5;
	
	SubMachineState root;
	LinkedList<PacManAction> pacmanActions = new LinkedList<PacManAction>();
	LinkedList<Action> resultActions = new LinkedList<Action>();
	
	Game game;
	
	public MyPacMan() {
		root = new SubMachineState();
		
		// Build states
		PacManState eatPillState = new PacManState(new NearestPillAction());
		root.addState(eatPillState);
		root.initialState = root.currentState = eatPillState;
		
		SubMachineState handleGhostState = new SubMachineState();
		root.addState(handleGhostState);
		
		PacManState eatGhostState = new PacManState(new NearestPillAction()); // TODO: change to eat ghost action
		handleGhostState.addState(eatGhostState);
		
		SubMachineState avoidGhostState = new SubMachineState();
		handleGhostState.addState(avoidGhostState);
		handleGhostState.initialState = handleGhostState.currentState = avoidGhostState;
		
		PacManState runAwayState = new PacManState(new RunAwayAction());
		avoidGhostState.addState(runAwayState);
		avoidGhostState.initialState = avoidGhostState.currentState = runAwayState;
		
		PacManState eatPowerPillState = new PacManState(new NearestPillAction()); // TODO: change to eat power pill action
		avoidGhostState.addState(eatPowerPillState);
		
		
		// Build Transitions
		PacManTransition close = new PacManTransition(game, new PacManCondition() {

			@Override
			public boolean test(Game game) {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					
					if (game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i)) <= CLOSE_DIST) {
						return true;
					}
				}
				return false;
			}
			
		});
		
		PacManTransition far = new PacManTransition(game, new PacManCondition() {

			@Override
			public boolean test(Game game) {
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					if (game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i)) <= CLOSE_DIST) {
						return false;
					}
				}
				return true;
			}
			
		});
		
		PacManTransition nearBlue = new PacManTransition(game, new PacManCondition() {

			@Override
			public boolean test(Game game) {
				int closest = -1;
				int dist = Integer.MAX_VALUE;
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					int ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
					
					if (ghostDist < dist) {
						dist = ghostDist;
						closest = i;
					}
				}
				
				return game.isEdible(closest);
			}
			
		});
		
		PacManTransition nearNonBlue = new PacManTransition(game, new PacManCondition() {

			@Override
			public boolean test(Game game) {
				int closest = -1;
				int dist = Integer.MAX_VALUE;
				for (int i = 0; i < Game.NUM_GHOSTS; i++) {
					int ghostDist = game.getPathDistance(game.getCurPacManLoc(), game.getCurGhostLoc(i));
					
					if (ghostDist < dist) {
						dist = ghostDist;
						closest = i;
					}
				}
				
				return !game.isEdible(closest);
			}
			
		});
		
		PacManTransition nearPower = new PacManTransition(game, new PacManCondition() {

			@Override
			public boolean test(Game game) {
				int[] powerPills = game.getPowerPillIndicesActive();
				
				for (int i = 0; i < powerPills.length; i++) {
					if (game.getPathDistance(powerPills[i], game.getCurPacManLoc()) < CLOSE_DIST) {
						return true;
					}
				}
				
				return false;
			}
			
		});
		
		// Join states with transitions
		bindTransition(eatPillState, handleGhostState, close);
		bindTransition(handleGhostState, eatPillState, far);
		
		bindTransition(avoidGhostState, eatGhostState, nearBlue);
		bindTransition(eatGhostState, avoidGhostState, nearNonBlue);
		
		bindTransition(runAwayState, eatPowerPillState, nearPower);
		
	}
	
	//Place your game logic here to play the game as Ms Pac-Man
	public int getAction(Game game, long timeDue)
	{		
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