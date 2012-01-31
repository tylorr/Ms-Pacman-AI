package game.entries.ghosts;
import game.core.*;
import game.core.Game.DM;

public class ChaseAction extends Action {
    
	public int OrangeGhostDirection(){
		int direction;
		/*
		if (pacManGame.getGhostPathDistance(3,pacManDirection) > 15)
		  direction = getNextGhostDir(3,pacManDirection,true,Game.DM.PATH);
		else if (pacManGame.getGhostPathDistance(3,pacManDirection) > 7 && pacManGame.getGhostPathDistance(3,pacManDirection) < 15)
		  direction = getNextGhostDir(3,getNextPacManDir(true,PATH),true,PATH);
		else
		  direction = getNextGhostDir(3,pacManDirection,true,PATH); 	
		  */
		return -1;
	}
	
	public int[] Chase(){
		/*
	  int[] directions = new int[Game.NUM_GHOSTS];
	  //make red follow pac man
	  directions[0] = pacManGame.getNextGhostDir(0,pacManDirection,true,PATH);	 
	  //make pink flank pac man
	  directions[1] = pacManGame.getNextGhostDir(1,getNextPacManDir(true,PATH),true,PATH);
	  //make blue chase pac man as usual
	  directions[2] = pacManGame.getNextGhostDir(2,(pacManGame.getGhostPathDistance(1,pacManDirection + 2)*2),true,pacManGame.PATH); 
	  //make orange follow red, but flank and follow alongside pac man and then corner him
	  directions[3] = getNextGhostDir(3,OrangeGhostDirection(),true,PATH);
	  return directions;
	  */
		return null;
  }
}
