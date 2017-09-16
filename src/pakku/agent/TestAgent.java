package pakku.agent;

import game.controllers.HeroController;
import game.core.G;
import game.core.Game;

public class TestAgent implements HeroController//, Constants
{
	public int getAction(Game game, long time)
	{
		int[] directions=game.getHero().getPossibleDirs(false);		//set flag as true to include reversals
		return directions[G.rnd.nextInt(directions.length)];
	}
}