package game.controllers.examples;

import game.controllers.EnemyController;
import game.core.Game;
import game.core.Node;

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
    	Node pacmanLoc = game.getCurHeroLoc();
    	
        for(int i=0;i<dirs.length;i++)      
        {
        	if(game.enemyRequiresAction(i))
        	{
        		//if ghosts are all in close proximity and not near Ms Pac-Man, disperse
        		if(isCrowded(game) && !closeToMsPacMan(game,game.getCurEnemyLoc(i)))
        			dirs[i]=getRetreatActions(game,i);                          				//go towards the power pill locations
        		//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
        		else if(game.getEdibleTime(i)>0 || closeToPower(game))
        			dirs[i]=game.getNextEnemyDir(i,pacmanLoc,false,Game.DM.PATH);      			//move away from ms pacman
        		//else go towards Ms Pac-Man
        		else        		
        			dirs[i]=game.getNextEnemyDir(i,pacmanLoc,true,Game.DM.PATH);       			//go towards ms pacman
        	}
        }
        
        return dirs;
    }

    private boolean closeToPower(Game game)
    {
    	Node pacmanLoc = game.getCurHeroLoc();
    	Node[] powerPills = game.getPowerPillNodes();
    	
    	for(int i=0;i<powerPills.length;i++)
    		if(game.checkPowerPill(i) && game.getPathDistance(powerPills[i], pacmanLoc) < PILL_PROXIMITY)
    			return true;

        return false;
    }

    private boolean closeToMsPacMan(Game game, Node location)
    {
    	if(game.getPathDistance(game.getCurHeroLoc(), location) < PACMAN_DISTANCE)
    		return true;

    	return false;
    }

    private boolean isCrowded(Game game)
    {
        float distance=0;

        for (int i = 0; i<Game.NUM_ENEMY -1; i++)
            for(int j = i+1; j<Game.NUM_ENEMY; j++)
                distance+=game.getPathDistance(game.getCurEnemyLoc(i),game.getCurEnemyLoc(j));
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

    private int getRetreatActions(Game game,int index)
    {
        if(game.getEdibleTime(index)==0 && game.getPathDistance(game.getCurEnemyLoc(index),game.getCurHeroLoc())<PACMAN_DISTANCE)
            return game.getNextEnemyDir(index,game.getCurHeroLoc(),true,Game.DM.PATH);
        else
            return game.getNextEnemyDir(index,game.getPowerPillNodes()[index],true,Game.DM.PATH);
    }
}