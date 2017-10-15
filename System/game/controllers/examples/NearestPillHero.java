package game.controllers.examples;

import java.util.ArrayList;

import game.controllers.HeroController;
import game.models.Game;
import game.models.Node;
import game.models.Hero;
import java.util.List;

public class NearestPillHero implements HeroController
{
	private int action;
	public int getAction() { return action; }
	public void init() { }
	public void shutdown() { }
	public void update(Game game,long timeDue)
	{
		List<Node> pills = game.getCurMaze().getPillNodes();
		List<Node> powerPills=game.getCurMaze().getPowerPillNodes();
		Hero hero = game.getHero();
		
		ArrayList<Node> targets=new ArrayList<Node>();

		for (Node pill : pills)
			if(game.checkPill(pill))
				targets.add(pill);

		for (Node pill : powerPills)
			if(game.checkPowerPill(pill))
				targets.add(pill);
		
		//return the next direction once the closest target has been identified
		action = game.getHero().getNextDir(hero.getTarget(targets,true), true);
	}
}