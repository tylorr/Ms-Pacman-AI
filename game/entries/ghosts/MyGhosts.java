package game.entries.ghosts;

import game.entries.ghosts.*;
import game.controllers.GhostController;
import game.core.Game;
import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyGhosts implements GhostController{
	
	 Decision root;
	 	 
	 public MyGhosts(){
		 Action runAway = new RunAwayAction();
		 Action chaseAfter = new ChaseAction();
		 Decision powerPill = new Decision();
		 root = new Decision();
		 root.threshold = 6;
		 root.testValue = new DistanceToPacMan();
		 root.trueAction = runAway;
		 root.falseNode = powerPill;
		 powerPill.threshold = 6;
		 powerPill.testValue = new DistanceToPacMan();
		 powerPill.trueAction = runAway;
		 powerPill.falseAction = chaseAfter;
	 }
	//Place your game logic here to play the game as the ghosts
	public int[] getActions(Game game,long timeDue)
	{
		return null;
	}
}