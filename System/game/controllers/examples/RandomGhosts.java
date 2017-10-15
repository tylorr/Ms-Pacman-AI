package game.controllers.examples;

import game.controllers.EnemyController;
import game.models.Game;
import game.models.Enemy;
import java.util.List;

public final class RandomGhosts implements EnemyController
{
	private int[] actions;
	public int[] getActions() { return actions; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		actions = new int[Game.NUM_ENEMY];
		Enemy[] enemies = (Enemy[]) game.getEnemies().toArray();
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 0; i < actions.length; i++)
		{
			Enemy enemy = enemies[i];
			if(enemy.requiresAction())
			{
				List<Integer> possibleDirs = enemy.getPossibleDirs();
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			}

		}
	}
}