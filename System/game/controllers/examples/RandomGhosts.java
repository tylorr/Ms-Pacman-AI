package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.G;
import game.core.Game;
import game.core.Enemy;

public final class RandomGhosts implements EnemyController
{	
	public int[] getActions(Game game,long timeDue)
	{	
		int[] directions = new int[Game.NUM_ENEMY];
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 0; i < directions.length; i++)
		{
			Enemy enemy = game.getEnemy(i);
			if(enemy.requiresAction())
			{
				int[] possibleDirs = enemy.getPossibleDirs();
				directions[i]=possibleDirs[G.rnd.nextInt(possibleDirs.length)];
			}

		}

		return directions;
	}
}