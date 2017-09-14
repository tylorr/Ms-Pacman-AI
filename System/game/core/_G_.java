/*
 * Implementation of "Ms Pac-Man" for the "Ms Pac-Man versus Ghost Team Competition", brought
 * to you by Philipp Rohlfshagen, David Robles and Simon Lucas of the University of Essex.
 * 
 * www.pacman-vs-ghosts.net
 * 
 * Code written by Philipp Rohlfshagen, based on earlier implementations of the game by
 * Simon Lucas and David Robles. 
 * 
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package game.core;

import java.util.BitSet;

public class _G_ extends G
{
	public static final int EDIBLE_ALERT=30;	//for display only (ghosts turning blue)
		
	//Instantiates everything to start a new game
	public void newGame()
	{	
		init();		//load mazes if not yet loaded
		
		curMaze=0;
		
		curEnemyLocs = new Node[G.NUM_ENEMY];
		lastHeroDirs = new int[G.NUM_ENEMY];
		edibleTimes = new int[G.NUM_ENEMY];
		lairTimes = new int[G.NUM_ENEMY];

		pills=new BitSet(getNumberPills());
		pills.set(0,getNumberPills());
		powerPills=new BitSet(getNumberPowerPills());
		powerPills.set(0,getNumberPowerPills());
		score=0;
		levelTime=0;
		totalTime=0;
		totLevel=0;
		livesRemaining=G.NUM_LIVES;
		extraLife=false;
		gameOver=false;
		
		reset(false);
	}
	
	//Size of the Maze (for display only)
	public int getWidth()
	{
		return mazes[curMaze].width;
	}
	
	//Size of the Maze (for display only)
	public int getHeight()
	{
		return mazes[curMaze].height;
	}
}