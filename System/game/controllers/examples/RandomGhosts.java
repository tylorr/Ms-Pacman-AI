package game.controllers.examples;

import game.controllers.EnemyController;
import game.system._Game;
import game.models.Game;
import game.models.Enemy;

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
				directions[i]=possibleDirs[Game.rng.nextInt(possibleDirs.length)];
			}

		}

		return directions;
	}
}