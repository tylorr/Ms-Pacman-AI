package ai.fun.ghosts;

import game.core.Game;
import java.util.*;

public class ChaseNewRouteAction extends ChaseAction{
	int[] dirNum = new int[4];
	public int PinkGhostLocationMod(Game game,int whichGhost){
		int direction;
		Random generator = new Random();
		int randomIndex = generator.nextInt(4);
		if (game.getPathDistance(game.getCurGhostLoc(whichGhost),getNextPacManNode(game)) > 8)
			direction = game.getNextGhostDir(whichGhost,getNextPacManNode(game),true,Game.DM.PATH);
		else
			direction = randomIndex;
		return direction;
	}
	public int OrangeGhostLocationAlmostOriginal(Game game,int whichGhost){
		int direction;
		Random generator = new Random();
		int randomIndex = generator.nextInt(4);
		if (game.getPathDistance(game.getCurGhostLoc(whichGhost),game.getCurPacManLoc()) > 8)
			direction = game.getNextGhostDir(whichGhost,game.getCurPacManLoc(),true,Game.DM.PATH);
		else
			direction = randomIndex;
		return direction;
	}
	public int OrangeGhostLocationMod(Game game,int whichGhost){
		int direction;
		if (game.getEuclideanDistance(game.getCurGhostLoc(whichGhost),game.getCurPacManLoc()) > 7 && 
				game.getEuclideanDistance(game.getCurGhostLoc(whichGhost),game.getCurPacManLoc()) < 15)
			direction = game.getNextGhostDir(whichGhost,getNextPacManNode(game),true,Game.DM.PATH);
		else
			direction = game.getNextGhostDir(whichGhost,game.getCurGhostLoc(0),true,Game.DM.PATH);
		return direction;
	}
	public int[] execute(Game game){
		int[] direction = new int[Game.NUM_GHOSTS];
		int i;
		for (i = 0; i < Game.NUM_GHOSTS; i++){
			if (game.getLairTime(i) > 2 && game.getLairTime(i) < 3)
				direction[i] = getDirection(game,i);
		}
		return direction;
	}
	public int getDirection(Game game, int whichGhost){
		dirNum[0] = game.getCurGhostDir(0);
		dirNum[1] = game.getCurGhostDir(1);
		dirNum[2] = game.getCurGhostDir(2);
		dirNum[3] = game.getCurGhostDir(3);
		int direction = 0;
		int i = 0;
		Random generator = new Random();
		int randomIndex = 0; 
		boolean newDirection = false;
		boolean sameDir = false;
		while (newDirection == false){
			if(getNextPacManNode(game) == -1){
				direction = 0;
				newDirection = true;
			}
			else{  	 
				randomIndex = generator.nextInt(8);
				if(randomIndex == 0)
					direction = game.getNextGhostDir(whichGhost,game.getCurPacManLoc(),true,Game.DM.PATH);
				else if (randomIndex == 1)
					direction = game.getNextGhostDir(whichGhost,getNextPacManNode(game),true,Game.DM.PATH);
				else if (randomIndex == 2)
					if(game.getCurGhostDir(3) == -1 || game.getGhostPathDistance(3, game.getCurPacManLoc()) < 4)
						direction = game.getNextGhostDir(0,game.getCurPacManLoc(),true,Game.DM.PATH);
					else
						direction = game.getNextGhostDir(3,game.getCurPacManLoc(),true,Game.DM.PATH);  
				else if (randomIndex == 3)
					direction = game.getNextGhostDir(2,OrangeGhostLocation(game,whichGhost),true,Game.DM.PATH);
				else if (randomIndex == 4)
					if(game.getCurGhostDir(3) == -1 || game.getGhostPathDistance(3, game.getCurPacManLoc()) < 25)
						direction = game.getNextGhostDir(0,game.getCurPacManLoc(),true,Game.DM.PATH);
					else
						direction = game.getNextGhostDir(3,game.getCurPacManLoc(),true,Game.DM.PATH);  
				else if (randomIndex == 5)
					direction = game.getNextGhostDir(whichGhost,OrangeGhostLocationMod(game,whichGhost),true,Game.DM.PATH);//change it!!
					else if (randomIndex == 6)
						direction = game.getNextGhostDir(whichGhost,OrangeGhostLocationAlmostOriginal(game,whichGhost),true,Game.DM.PATH);//change it!!
					else
						direction = game.getNextGhostDir(whichGhost,PinkGhostLocationMod(game,whichGhost),true,Game.DM.PATH);//change it!!
				for(i = 0; i < Game.NUM_GHOSTS; i++){
					if (i != whichGhost){
						if(dirNum[i] == direction)
							sameDir = true;
					}
				}
				if(sameDir == false)
					newDirection = true;
			}
		}
		return direction;
	}
}
