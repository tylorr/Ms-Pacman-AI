package game.controllers.examples;

import game.controllers.DefenderController;
import game.models.Game;
import game.models.Defender;
import java.util.List;

public final class RandomDefenders implements DefenderController
{
	public void init(Game game) { }
	public void shutdown(Game game) { }
	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
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
		return actions;
	}
}