package game.controllers.examples;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import game.controllers.HeroController;
import game.models.Game;
import game.models.Game;
import game.system.GameView;
import game.models.Node;
import game.models.Enemy;
import game.models.Hero;

/*
 * Same as NearestPillHero but does some visuals to illustrate what can be done.
 * Please note: the visuals are just to highlight different functionalities and may
 * not make sense from a controller's point of view (i.e., they might not be useful)
 * Comment/un-comment code below as desired (drawing all visuals would probably be too much).
 */
public final class NearestPillHeroVS implements HeroController
{	
	public int getAction(Game game,long timeDue)
	{		
		List<Node> pills = game.getCurMaze().getPillNodes();
		List<Node> powerPills = game.getCurMaze().getPowerPillNodes();
		Hero hero = game.getHero();

		ArrayList<Node> targets = new ArrayList<Node>();
		
		for (Node pill : pills)
			if(game.checkPill(pill))
				targets.add(pill);
		
		for(Node pill : powerPills)
			if(game.checkPowerPill(pill))
				targets.add(pill);

		Node[] targetsArray=new Node[targets.size()];
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i] = targets.get(i);
		
		Node nearest = hero.getTarget(targetsArray,true);
		
		//add the path that Ms Pac-Man is following
//		GameView.addPoints(game,Color.GREEN,game.getPath(current,nearest));
		
		//add the path from Ms Pac-Man to the first power pill
		GameView.addPoints(game, Color.CYAN, hero.getPath(powerPills.get(0)));
		
		//add the path AND ghost path from Ghost 0 to the first power pill (to illustrate the differences)
//		if(game.getLairTime(0)==0)
//		{
//			GameView.addPoints(game,Color.ORANGE,game.getPath(game.getCurEnemyLoc(0),powerPills[0]));
//			GameView.addPoints(game,Color.YELLOW,game.getEnemyPath(0,powerPills[0]));
//		}
		
		//add the path from Ghost 0 to the closest power pill
//		if(game.getLairTime(0)==0)
//			GameView.addPoints(game,Color.WHITE,game.getEnemyPath(0,game.getEnemyTarget(0,powerPills,true)));
		
		//add lines connecting Ms Pac-Man and the power pills
//		for(int i=0;i<powerPills.length;i++)
//			GameView.addLines(game,Color.CYAN,current,powerPills[i]);
		
		//add lines to the ghosts (if not in lair) - green if edible, red otherwise
		for(int i = 0; i< Game.NUM_ENEMY; i++)
		{
			Enemy enemy = game.getEnemy(i);
			if(enemy.getLairTime() == 0)
				if(enemy.isEdible())
					GameView.addLines(game, Color.GREEN, hero.getLocation(), enemy.getLocation());
				else
					GameView.addLines(game, Color.RED, hero.getLocation(), enemy.getLocation());

		}

		return game.getHero().getNextDir(nearest, true);
	}
}