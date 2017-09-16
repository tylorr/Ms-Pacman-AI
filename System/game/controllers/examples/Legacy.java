package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.Game.DM;
import game.core.Game;
import game.core.G;

public class Legacy implements EnemyController
{
	public int[] getActions(Game game,long timeDue)
	{
		int[] directions=new int[Game.NUM_ENEMY];
		DM[] dms=Game.DM.values();
		
		for(int i=0;i<directions.length-1;i++)
			if(game.enemyRequiresAction(i))
				directions[i]=game.getNextEnemyDir(i, game.getHero().getLocation(), true,dms[i]);	//approach Ms Pac-Man using a different distance measure
																							//for each ghost; last ghost takes random action
		directions[3]=G.rnd.nextInt(4);
		
		return directions;
	}
}