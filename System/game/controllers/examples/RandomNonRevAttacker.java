package game.controllers.examples;

import game.controllers.AttackerController;
import game.models.Game;

import java.util.List;

public final class RandomNonRevAttacker implements AttackerController
{
	public void init(Game game) { }
	public void shutdown(Game game) { }
	public int update(Game game,long timeDue)
	{
		List<Integer> directions = game.getAttacker().getPossibleDirs(false);		//set flag as false to prevent reversals
		return directions.get(Game.rng.nextInt(directions.size()));
	}
}