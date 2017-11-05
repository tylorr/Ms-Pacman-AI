package game.controllers.examples;

import java.util.List;

import game.controllers.DefenderController;
import game.models.Game;
import game.models.Node;
import game.models.Defender;

public class Legacy2TheReckoning implements DefenderController
{
	public static final int CROWDED_DISTANCE=30;
	public static final int PACMAN_DISTANCE=10;
    public static final int PILL_PROXIMITY=15;

    private int[] actions;

    public Legacy2TheReckoning()
    {
        actions =new int[Game.NUM_DEFENDER];
    }

    public void init(Game game) { }
    public void shutdown(Game game) { }
    public int[] update(Game game,long timeDue)
    {
    	Node pacmanLoc = game.getAttacker().getLocation();

        for(int i = 0; i< actions.length; i++)
        {
            Defender defender = game.getDefender(i);

        	if(defender.requiresAction())
        	{
        		//if ghosts are all in close proximity and not near Ms Pac-Man, disperse
        		if(isCrowded(game) && !closeToMsPacMan(game, defender.getLocation()))
        			actions[i]=getRetreatActions(game,i);                          				//go towards the power pill locations
        		//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
        		else if(defender.getVulnerableTime() > 0 || closeToPower(game))
        			actions[i] = game.getDefender(i).getNextDir(pacmanLoc, false);      			//move away from ms pacman
        		//else go towards Ms Pac-Man
        		else
                    actions[i] = game.getDefender(i).getNextDir(pacmanLoc, true);      			//go towards ms pacman
        	}
        }

        return actions;
    }

    private boolean closeToPower(Game game)
    {
    	Node pacmanLoc = game.getAttacker().getLocation();
    	List<Node> powerPills = game.getCurMaze().getPowerPillNodes();

    	for (Node pill : powerPills)
    		if(game.checkPowerPill(pill) && pill.getPathDistance(pacmanLoc) < PILL_PROXIMITY)
    			return true;

        return false;
    }

    private boolean closeToMsPacMan(Game game, Node location)
    {
    	if(game.getAttacker().getLocation().getPathDistance(location) < PACMAN_DISTANCE)
    		return true;

    	return false;
    }

    private boolean isCrowded(Game game)
    {
        float distance=0;

        for (int i = 0; i<Game.NUM_DEFENDER -1; i++)
            for(int j = i+1; j<Game.NUM_DEFENDER; j++)
                distance+=game.getDefender(i).getLocation().getPathDistance(game.getDefender(j).getLocation());
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

    private int getRetreatActions(Game game,int index)
    {
        Defender defender = game.getDefender(index);

        if(defender.getVulnerableTime() == 0 && defender.getLocation().getPathDistance(game.getAttacker().getLocation()) < PACMAN_DISTANCE)
            return defender.getNextDir(game.getAttacker().getLocation(), true);
        else
            return defender.getNextDir(game.getCurMaze().getPowerPillNodes().get(index), true);
    }
}