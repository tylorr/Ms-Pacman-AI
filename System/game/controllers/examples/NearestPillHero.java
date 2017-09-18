package game.controllers.examples;

import java.util.ArrayList;

import game.controllers.HeroController;
import game.core.G;
import game.core.Game;
import game.core.Node;

public class NearestPillHero implements HeroController
{	
	public int getAction(Game game,long timeDue)
	{	
		Node[] pills = game.getPillNodes();
		Node[] powerPills=game.getPowerPillNodes();
		Node current = game.getHero().getLocation();
		
		ArrayList<Node> targets=new ArrayList<Node>();
		
		for(int i=0;i<pills.length;i++)			//check which pills are available			
			if(game.checkPill(i))
				targets.add(pills[i]);
		
		for(int i=0;i<powerPills.length;i++)	//check with power pills are available
			if(game.checkPowerPill(i))
				targets.add(powerPills[i]);
		
		Node[] targetsArray=new Node[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		return game.getHero().getNextDir(game.getTarget(current, targetsArray,true,G.DM.PATH), true);
	}
}