package game.controllers.examples;

import game.controllers.DefenderController;
import game.models.Game.DM;
import game.models.Game;
import game.models.Defender;

public class Legacy implements DefenderController
{
	private int[] actions;
	public int[] getActions() { return actions; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		actions=new int[Game.NUM_DEFENDER];
		DM[] dms=Game.DM.values();

		Defender[] enemies = (Defender[]) game.getDefenders().toArray();

		for(int i=0;i<actions.length-1;i++)
		{
			Defender defender = enemies[i];
			if (defender.requiresAction())
				actions[i] = defender.getNextDir(game.getAttacker().getLocation(), true);
			//for each ghost; last ghost takes random action
		}
		actions[3]= Game.rng.nextInt(4);
	}
}