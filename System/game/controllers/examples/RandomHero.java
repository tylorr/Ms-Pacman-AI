package game.controllers.examples;

import java.util.List;
import game.controllers.HeroController;
import game.models.Game;

public final class RandomHero implements HeroController
{
	private int action;
	public int getAction() { return action; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		List<Integer> directions=game.getHero().getPossibleDirs(true);		//set flag as true to include reversals
		action = directions.get(Game.rng.nextInt(directions.size()));
	}
}