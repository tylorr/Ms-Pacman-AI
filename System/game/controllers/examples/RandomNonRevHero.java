package game.controllers.examples;

import game.controllers.HeroController;
import game.core.G;
import game.core.Game;

public final class RandomNonRevHero implements HeroController
{	
	public int getAction(Game game,long timeDue)
	{			
		int[] directions=game.getHero().getPossibleDirs(false);		//set flag as false to prevent reversals
		return directions[G.rnd.nextInt(directions.length)];		
	}
}