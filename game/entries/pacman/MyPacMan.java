package game.entries.pacman;

import ai.fsm.Action;
import ai.fsm.StateMachine;
import ai.fsm.StateMachineState;
import game.controllers.PacManController;
import game.core.G;
import game.core.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan implements PacManController
{
	class PacmanState extends StateMachineState {
		PacmanAction action;
		
		public PacmanState(PacmanAction action) {
			this.action = action;
		}
		
		@Override
		public Action getActions() {
			return action;
		}
		
	}
	
	abstract class PacmanAction extends Action {		
		abstract int act(Game game);
	}
	
	class NearestPillAction extends PacmanAction {
		@Override
		public int act(Game game) {
			int current=game.getCurPacManLoc();
			
			//get all active pills
			int[] activePills=game.getPillIndicesActive();
			
			//get all active power pills
			int[] activePowerPills=game.getPowerPillIndicesActive();
			
			//create a target array that includes all ACTIVE pills and power pills
			int[] targetsArray=new int[activePills.length+activePowerPills.length];
			
			for(int i=0;i<activePills.length;i++)
				targetsArray[i]=activePills[i];
			
			for(int i=0;i<activePowerPills.length;i++)
				targetsArray[activePills.length+i]=activePowerPills[i];		
			
			//return the next direction once the closest target has been identified
			return game.getNextPacManDir(game.getTarget(current,targetsArray,true,G.DM.PATH),true,Game.DM.PATH);
		}
	}
	
	private StateMachine sm;
	private PacmanAction actions = null;
	
	public MyPacMan() {
		//TODO: build FSM
		sm = new StateMachine();
		
		sm.initialState = new PacmanState(new NearestPillAction());
		sm.currentState = sm.initialState;
	}
	//Place your game logic here to play the game as Ms Pac-Man
	public int getAction(Game game,long timeDue)
	{
		if (actions == null) {
			actions = (PacmanAction)sm.update();
		}
		int result = actions.act(game);
		actions = (PacmanAction) actions.next;
		return result;
	}
}