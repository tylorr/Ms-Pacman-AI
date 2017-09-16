package game.controllers.examples;

import game.controllers.HeroController;
import game.core.G;
import game.core.Game;

public final class RandomHero implements HeroController
{
	public int getAction(Game game,long timeDue)
	{
		int[] directions=game.getHero().getPossibleDirs(true);		//set flag as true to include reversals
		return directions[G.rnd.nextInt(directions.length)];
	}
}