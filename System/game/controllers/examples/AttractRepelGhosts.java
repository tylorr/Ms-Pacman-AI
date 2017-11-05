package game.controllers.examples;

import java.util.List;

import game.controllers.DefenderController;
import game.models.*;

public final class AttractRepelGhosts implements DefenderController
{	
	private final static float CONSISTENCY=0.9f;	//move towards/away with this probability
	private boolean attract;

	public AttractRepelGhosts(boolean attract)	//Please note: constructors CANNOT take arguments in the competition!
	{
		this.attract=attract;	//approach or retreat from Ms Pac-Man
	}

	public void init(Game game) { }
	public void shutdown(Game game) { }
	public int[] update(Game game, long timeDue)
	{		
		int[] actions = new int[Game.NUM_DEFENDER];

		Defender[] enemies = (Defender[]) game.getDefenders().toArray();
		for(int i=0;i<actions.length;i++)	//for each ghost
		{
			Defender defender = enemies[i];
			if (defender.requiresAction())        //if it requires an action
			{
				if (Game.rng.nextFloat() < CONSISTENCY)    //approach/retreat from the current node that Ms Pac-Man is at
					actions[i] = defender.getNextDir(game.getAttacker().getLocation(), attract);
				else                                    //else take a random action
				{
					List<Integer> possibleDirs = defender.getPossibleDirs();    //takes a random LEGAL action. Could also just return any random number
					actions[i] = possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
				}
			}
		}

		return actions;
	}
}