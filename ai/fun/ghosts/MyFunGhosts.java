package ai.fun.ghosts;

import game.controllers.GhostController;
import game.core.Game;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyFunGhosts implements GhostController{
	
	 Decision rootIsBlue;
	 	 
	 public MyFunGhosts(){
		 Action chasePacMan = new ChaseAction();
		 Action chaseNewDir = new ChaseNewRouteAction();
		 Action runAway = new RunAwayAction();
		 Decision ate = new IsEaten();
		 rootIsBlue = new IsBlue();
		 rootIsBlue.trueNode = ate;
		 rootIsBlue.falseNode = chasePacMan;
		 ate.falseNode = runAway;
		 ate.trueNode = chaseNewDir;
		 
	 }
	//Place your game logic here to play the game as the ghosts
	public int[] getActions(Game game,long timeDue)
	{
		Action nextAction = (Action) rootIsBlue.makeDecision(game); //exception here
		return nextAction.execute(game);		
		
	}
}
