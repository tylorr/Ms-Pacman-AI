package pakku.agent;

import game.controllers.HeroController;
import game.system._Game;
import game.models.Game;

public class TestAgent implements HeroController//, Constants
{
	public int getAction(Game game, long time)
	{
		int[] directions=game.getHero().getPossibleDirs(false);		//set flag as true to include reversals
		return directions[Game.rng.nextInt(directions.length)];
	}
}