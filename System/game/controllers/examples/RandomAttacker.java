package game.controllers.examples;

import java.util.List;

import game.controllers.AttackerController;
import game.models.Game;

public final class RandomAttacker implements AttackerController
{
	public void init(Game game) { }
	public void shutdown(Game game) { }

	public int update(Game game,long timeDue)
	{
		List<Integer> directions=game.getAttacker().getPossibleDirs(true);		//set flag as true to include reversals
		return directions.get(Game.rng.nextInt(directions.size()));
	}
}