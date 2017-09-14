package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.G;
import game.core.Game;

public final class AttractRepelGhosts implements EnemyController
{	
	private final static float CONSISTENCY=0.9f;	//move towards/away with this probability
	private boolean attract;
	
	public AttractRepelGhosts(boolean attract)	//Please note: constructors CANNOT take arguments in the competition!
	{
		this.attract=attract;	//approach or retreat from Ms Pac-Man
	}
	
	public int[] getActions(Game game,long timeDue)
	{		
		int[] directions=new int[Game.NUM_ENEMY];
		
		for(int i=0;i<directions.length;i++)	//for each ghost
			if(game.enemyRequiresAction(i))		//if it requires an action
			{
				if(G.rnd.nextFloat()<CONSISTENCY)	//approach/retreat from the current node that Ms Pac-Man is at
					directions[i]=game.getNextEnemyDir(i,game.getCurHeroLoc(),attract,Game.DM.PATH);
				else									//else take a random action
				{					
					int[] possibleDirs=game.getPossibleEnemyDirs(i);	//takes a random LEGAL action. Could also just return any random number
					directions[i]=possibleDirs[G.rnd.nextInt(possibleDirs.length)];
				}
			}

		return directions;
	}
}