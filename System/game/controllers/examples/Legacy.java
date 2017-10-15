package game.controllers.examples;

import game.controllers.EnemyController;
import game.models.Game.DM;
import game.models.Game;
import game.system._Game;
import game.models.Enemy;

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
		directions[3]= Game.rng.nextInt(4);
		
		return directions;
	}
}