package game.entries.ghosts;
import game.core.*;

public class RunAwayAction extends Action{
  //don't aim for any of pac man's neighbor nodes
  //go for the corners if pac man is far away from you
  //blue's original path could be useful in this	
  //search for the closest decision nodes, figure out distance between each
  //pick the one farthest away from Ms. Pac-Man	
  public int[] run(){
	  /*
	  int[] directions = new int[Game.NUM_GHOSTS];
	  for(int i=0;i<directions.length;i++)		//for each ghost
		if(game.ghostRequiresAction(i))			//if it requires an action
		  directions[i]=game.getNextGhostDir(i,game.getCurPacManLoc(),false,Game.DM.PATH);

		return directions;
		*/
	  return null;
  }
}
