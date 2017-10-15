package game.controllers.examples;

import game.controllers.EnemyController;
import game.models.Game.DM;
import game.models.Game;
import game.system._Game;
import game.models.Enemy;

public class Legacy implements EnemyController
{
	private int[] actions;
	public int[] getActions() { return actions; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		actions=new int[Game.NUM_ENEMY];
		DM[] dms=Game.DM.values();

		Enemy[] enemies = (Enemy[]) game.getEnemies().toArray();

		for(int i=0;i<actions.length-1;i++)
		{
			Enemy enemy = enemies[i];
			if (enemy.requiresAction())
				actions[i] = enemy.getNextDir(game.getHero().getLocation(), true);
			//for each ghost; last ghost takes random action
		}
		actions[3]= Game.rng.nextInt(4);
	}
}