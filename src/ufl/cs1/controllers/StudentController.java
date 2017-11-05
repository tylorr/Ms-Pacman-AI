package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;

import java.util.List;

public final class StudentController implements DefenderController
{
	private int[] actions;
	public int[] getActions() { return actions; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 0; i < actions.length; i++)
		{
			Defender defender = enemies.get(i);
			if(defender.requiresAction())
			{
				List<Integer> possibleDirs = defender.getPossibleDirs();
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			}

		}
	}
}