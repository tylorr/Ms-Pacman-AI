package game.entries.ghosts; //exception at Decision
import game.core.*;
import game.core.Game.DM;

public class ChaseAction extends Action{
    
	public int OrangeGhostLocation(Game game,int whichGhost){
		int location;
		if (game.getEuclideanDistance(game.getCurGhostLoc(whichGhost),game.getCurPacManLoc()) > 10)
		  location = getNextPacManNode(game);
		else
			location = game.getCurPacManLoc();
		return location;
	}
	
	public int getNextPacManNode(Game game){
		return game.getNeighbour(game.getCurPacManLoc(),game.getCurPacManDir());
	}
	
	public int[] execute(Game game){
	  int[] directions = new int[Game.NUM_GHOSTS]; 
	  //make red follow pac man
	  directions[0] = game.getNextGhostDir(0,game.getCurPacManLoc(),true,Game.DM.PATH);
	  if (getNextPacManNode(game) == -1){
		  directions[1] = 0;
          directions[3] = game.getCurPacManDir();
          directions[2] = game.getNextGhostDir(0,game.getCurPacManLoc(),true,Game.DM.PATH);
	  }	  
	  else{
		//make pink flank pac man  
	    directions[1] = game.getNextGhostDir(1,getNextPacManNode(game),true,Game.DM.PATH);
	    //make blue mimic pac man and go for the kill
	    if(game.getCurGhostDir(3) == -1 || game.getGhostPathDistance(3, game.getCurPacManLoc()) < 4)
	    	directions[3] = game.getNextGhostDir(0,game.getCurPacManLoc(),true,Game.DM.PATH);
	    else
	      directions[3] = game.getNextGhostDir(3,game.getCurPacManLoc(),true,Game.DM.PATH);   
	    //make orange follow red, but flank and follow alongside pac man and then corner him
	    directions[2] = game.getNextGhostDir(2,OrangeGhostLocation(game,2),true,Game.DM.PATH);
	  }
	  return directions;
  }

}
