package pakku.agent;

import game.controllers.HeroController;
import game.system._Game;
import game.models.Game;

import java.util.List;

public class TestAgent implements HeroController//, Constants
{
	private int action;
	public int getAction() { return action; }
	public void init() { }
	public void shutdown() { }

	public void update(Game game, long time)
	{
		List<Integer> directions = game.getHero().getPossibleDirs(false);		//set flag as true to include reversals
		action = directions.get(Game.rng.nextInt(directions.size()));
	}
}