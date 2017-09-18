package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.Game.DM;
import game.core.Game;
import game.core.G;
import game.core.Enemy;

public class Legacy implements EnemyController
{
	public int[] getActions(Game game,long timeDue)
	{
		int[] directions=new int[Game.NUM_ENEMY];
		DM[] dms=Game.DM.values();
		
		for(int i=0;i<directions.length-1;i++)
		{
			Enemy enemy = game.getEnemy(i);
			if (enemy.requiresAction())
				directions[i] = game.getEnemy(i).getNextDir(game.getHero().getLocation(), true);
			//for each ghost; last ghost takes random action
		}
		directions[3]=G.rnd.nextInt(4);
		
		return directions;
	}
}