package game.controllers.examples;

import game.controllers.DefenderController;
import game.models.Game;
import game.models.Defender;
import java.util.List;

public final class RandomDefenders implements DefenderController
{
	private int[] actions;
	public int[] getActions() { return actions; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		actions = new int[Game.NUM_DEFENDER];
		Defender[] enemies = (Defender[]) game.getDefenders().toArray();
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 0; i < actions.length; i++)
		{
			Defender defender = enemies[i];
			if(defender.requiresAction())
			{
				List<Integer> possibleDirs = defender.getPossibleDirs();
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			}

		}
	}
}