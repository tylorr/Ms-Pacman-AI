package game.controllers.examples;

import java.util.*;
import game.controllers.DefenderController;
import game.models.Game.DM;
import game.models.Game;
import game.models.Defender;

public class Legacy implements DefenderController
{
	public void init(Game game) { }
	public void shutdown(Game game) { }
	public int[] update(Game game,long timeDue)
	{
		int[] actions=new int[Game.NUM_DEFENDER];
		DM[] dms=Game.DM.values();

		List<Defender> enemies = game.getDefenders();

		for(int i=0;i<actions.length;i++)
		{
			Defender defender = enemies.get(i);
//			if (defender.requiresAction())
			actions[i] = defender.getNextDir(game.getAttacker().getLocation(), true);
			//for each ghost; last ghost takes random action
		}
//		actions[3] = Game.rng.nextInt(4);

		return actions;
	}
}