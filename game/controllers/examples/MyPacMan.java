package game.controllers.examples;

import java.util.ArrayList;
import game.controllers.PacManController;
import game.core.Game;

/*
 * Pac-Man controller as part of the starter package - simply upload this file as a zip called
 * MyPacMan.zip and you will be entered into the rankings - as simple as that!
 * Controller written by Philipp Rohlfshagen - feel free to modify it or to
 * start from scratch, using the classes supplied
 * with the original software. Best of luck!
 * 
 * This controller utilises 3 tactics, in order of importance:
 * 1. Get away from any non-edible ghost that is in close proximity
 * 2. Go after the nearest edible ghost
 * 3. Go to the nearest pill/power pill
 */
public class MyPacMan implements PacManController
{	
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away
	
	public int getAction(Game game,long timeDue)
	{			
		int current=game.getCurPacManLoc();
		
		//Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
		for(int i=0;i<Game.NUM_GHOSTS;i++)
			if(game.getEdibleTime(i)==0 && game.getLairTime(i)==0)
				if(game.getPathDistance(current,game.getCurGhostLoc(i))<MIN_DISTANCE)
					return game.getNextPacManDir(game.getCurGhostLoc(i),false,Game.DM.PATH);
		
		//Strategy 2: find the nearest edible ghost and go after them 
		int minDist=Integer.MAX_VALUE;
		int minGhost=-1;		
		
		for(int i=0;i<Game.NUM_GHOSTS;i++)
			if(game.getEdibleTime(i)>0)
			{
				int distance=game.getPathDistance(current,game.getCurGhostLoc(i));
				
				if(distance<minDist)
				{
					minDist=distance;
					minGhost=i;
				}
			}
		
		if(minGhost!=-1)	//we found an edible ghost
			return game.getNextPacManDir(game.getCurGhostLoc(minGhost),true,Game.DM.PATH);
		
		//Strategy 3: go after the pills and power pills
		int[] pills=game.getPillIndices();
		int[] powerPills=game.getPowerPillIndices();		
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)			//check which pills are available			
			if(game.checkPill(i))
				targets.add(pills[i]);
		
		for(int i=0;i<powerPills.length;i++)	//check with power pills are available
			if(game.checkPowerPill(i))
				targets.add(powerPills[i]);				
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		return game.getNextPacManDir(game.getTarget(current,targetsArray,true,Game.DM.PATH),true,Game.DM.PATH);	
	}
}
