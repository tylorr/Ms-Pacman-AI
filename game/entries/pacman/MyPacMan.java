package game.entries.pacman;

import ai.fsm.Action;
import ai.fsm.StateMachine;
import ai.fsm.StateMachineState;
import game.controllers.PacManController;
import game.core.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan implements PacManController
{
	class PacmanState extends StateMachineState {
		PacManAction action;
		
		public PacmanState(PacManAction action) {
			this.action = action;
		}
		
		@Override
		public Action getActions() {
			return action;
		}
		
	}
	
	private StateMachine sm;
	private PacManAction actions = null;
	
	public MyPacMan() {
		//TODO: build FSM
		sm = new StateMachine();
		
		sm.initialState = new PacmanState(new NearestPillAction());
		sm.currentState = sm.initialState;
	}
	//Place your game logic here to play the game as Ms Pac-Man
	public int getAction(Game game,long timeDue)
	{
		while (actions == null) {
			actions = (PacManAction)sm.update();
		}
		
		int result = actions.act(game);
		actions = (PacManAction) actions.next;
		return result;
	}
}