package game.controllers.examples;

import java.util.List;

import game.controllers.AttackerController;
import game.models.Game;

public final class RandomAttacker implements AttackerController
{
	private int action;
	public int getAction() { return action; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		List<Integer> directions=game.getAttacker().getPossibleDirs(true);		//set flag as true to include reversals
		action = directions.get(Game.rng.nextInt(directions.size()));
	}
}