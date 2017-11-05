package game.controllers.examples;

import game.controllers.AttackerController;
import game.models.Game;

import java.util.List;

public final class RandomNonRevAttacker implements AttackerController
{
	private int action;
	public int getAction() { return action; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		List<Integer> directions = game.getAttacker().getPossibleDirs(false);		//set flag as false to prevent reversals
		action = directions.get(Game.rng.nextInt(directions.size()));
	}
}