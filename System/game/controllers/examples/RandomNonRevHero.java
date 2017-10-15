package game.controllers.examples;

import game.controllers.HeroController;
import game.system._Game;
import game.models.Game;

public final class RandomNonRevHero implements HeroController
{	
	public int getAction(Game game,long timeDue)
	{			
		int[] directions=game.getHero().getPossibleDirs(false);		//set flag as false to prevent reversals
		return directions[Game.rng.nextInt(directions.length)];
	}
}