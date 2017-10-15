package game.controllers.examples;

import game.controllers.HeroController;
import game.system._Game;
import game.models.Game;

public final class RandomHero implements HeroController
{
	public int getAction(Game game, long timeDue)
	{
		int[] directions=game.getHero().getPossibleDirs(true);		//set flag as true to include reversals
		return directions[Game.rng.nextInt(directions.length)];
	}
}