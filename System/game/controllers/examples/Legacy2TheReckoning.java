package game.controllers.examples;

import java.util.List;
import game.controllers.EnemyController;
import game.models.Game;
import game.models.Node;
import game.models.Enemy;

public class Legacy2TheReckoning implements EnemyController
{
	public static final int CROWDED_DISTANCE=30;
	public static final int PACMAN_DISTANCE=10;
    public static final int PILL_PROXIMITY=15;

    private int[] actions;
    
    public Legacy2TheReckoning()
    {
        actions =new int[Game.NUM_ENEMY];
    }

    public int[] getActions() { return actions; }
    public void init() { }
    public void shutdown() { }
    public void update(Game game,long timeDue)
    {
    	Node pacmanLoc = game.getHero().getLocation();

        for(int i = 0; i< actions.length; i++)
        {
            Enemy enemy = game.getEnemy(i);

        	if(enemy.requiresAction())
        	{
        		//if ghosts are all in close proximity and not near Ms Pac-Man, disperse
        		if(isCrowded(game) && !closeToMsPacMan(game,enemy.getLocation()))
        			actions[i]=getRetreatActions(game,i);                          				//go towards the power pill locations
        		//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
        		else if(enemy.getEdibleTime() > 0 || closeToPower(game))
        			actions[i] = game.getEnemy(i).getNextDir(pacmanLoc, false);      			//move away from ms pacman
        		//else go towards Ms Pac-Man
        		else
                    actions[i] = game.getEnemy(i).getNextDir(pacmanLoc, true);      			//go towards ms pacman
        	}
        }
    }

    private boolean closeToPower(Game game)
    {
    	Node pacmanLoc = game.getHero().getLocation();
    	List<Node> powerPills = game.getCurMaze().getPowerPillNodes();

    	for (Node pill : powerPills)
    		if(game.checkPowerPill(pill) && pill.getPathDistance(pacmanLoc) < PILL_PROXIMITY)
    			return true;

        return false;
    }

    private boolean closeToMsPacMan(Game game, Node location)
    {
    	if(game.getHero().getLocation().getPathDistance(location) < PACMAN_DISTANCE)
    		return true;

    	return false;
    }

    private boolean isCrowded(Game game)
    {
        float distance=0;

        for (int i = 0; i<Game.NUM_ENEMY -1; i++)
            for(int j = i+1; j<Game.NUM_ENEMY; j++)
                distance+=game.getEnemy(i).getLocation().getPathDistance(game.getEnemy(j).getLocation());
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

    private int getRetreatActions(Game game,int index)
    {
        Enemy enemy = game.getEnemy(index);

        if(enemy.getEdibleTime() == 0 && enemy.getLocation().getPathDistance(game.getHero().getLocation()) < PACMAN_DISTANCE)
            return enemy.getNextDir(game.getHero().getLocation(), true);
        else
            return enemy.getNextDir(game.getCurMaze().getPowerPillNodes().get(index), true);
    }
}