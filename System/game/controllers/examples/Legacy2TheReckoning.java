package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.Game;
import game.core.Node;
import game.core.Enemy;

public class Legacy2TheReckoning implements EnemyController
{
	public static final int CROWDED_DISTANCE=30;
	public static final int PACMAN_DISTANCE=10;
    public static final int PILL_PROXIMITY=15;

    private final int[] dirs;
    
    public Legacy2TheReckoning()
    {
        dirs=new int[Game.NUM_ENEMY];
    }

    public int[] getActions(Game game,long timeDue)
    {
    	Node pacmanLoc = game.getHero().getLocation();

        for(int i=0;i<dirs.length;i++)
        {
            Enemy enemy = game.getEnemy(i);

        	if(enemy.requiresAction())
        	{
        		//if ghosts are all in close proximity and not near Ms Pac-Man, disperse
        		if(isCrowded(game) && !closeToMsPacMan(game,enemy.getLocation()))
        			dirs[i]=getRetreatActions(game,i);                          				//go towards the power pill locations
        		//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
        		else if(enemy.getEdibleTime() > 0 || closeToPower(game))
        			dirs[i] = game.getNextEnemyDir(i, pacmanLoc,false,Game.DM.PATH);      			//move away from ms pacman
        		//else go towards Ms Pac-Man
        		else        		
        			dirs[i] = game.getNextEnemyDir(i,pacmanLoc,true,Game.DM.PATH);       			//go towards ms pacman
        	}
        }
        
        return dirs;
    }

    private boolean closeToPower(Game game)
    {
    	Node pacmanLoc = game.getHero().getLocation();
    	Node[] powerPills = game.getPowerPillNodes();
    	
    	for(int i=0;i<powerPills.length;i++)
    		if(game.checkPowerPill(i) && game.getPathDistance(powerPills[i], pacmanLoc) < PILL_PROXIMITY)
    			return true;

        return false;
    }

    private boolean closeToMsPacMan(Game game, Node location)
    {
    	if(game.getPathDistance(game.getHero().getLocation(), location) < PACMAN_DISTANCE)
    		return true;

    	return false;
    }

    private boolean isCrowded(Game game)
    {
        float distance=0;

        for (int i = 0; i<Game.NUM_ENEMY -1; i++)
            for(int j = i+1; j<Game.NUM_ENEMY; j++)
                distance+=game.getPathDistance(game.getEnemy(i).getLocation(), game.getEnemy(j).getLocation());
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

    private int getRetreatActions(Game game,int index)
    {
        Enemy enemy = game.getEnemy(index);

        if(enemy.getEdibleTime() == 0 && game.getPathDistance(enemy.getLocation(), game.getHero().getLocation()) < PACMAN_DISTANCE)
            return game.getNextEnemyDir(index, game.getHero().getLocation(), true, Game.DM.PATH);
        else
            return game.getNextEnemyDir(index, game.getPowerPillNodes()[index], true, Game.DM.PATH);
    }
}