/*
 *
 * Code written by Philipp Rohlfshagen, based on earlier implementations of the game by
 * Simon Lucas and David Robles. 
 *
 * Code refactored and updated by Jeremiah Blanchard at the University of Florida (2017).
 *
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package game.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

/*
 * Simple implementation of the game. The class Game contains all code relating to the
 * game; the class GameView displays the game. Controllers must implement HeroController
 * and EnemyController respectively. The game may be executed using Exec.
 */
public class G implements Game
{	
	// Random number generator
	public static Random rnd = new Random(0);

	//File names for data
	//File names for data
	public static String[] nodeNames={"a","b","c","d"};
	public static String[] distNames={"da","db","dc","dd"};
	public static String pathMazes="data";
	
	//Static stuff (mazes are immutable - hence static)
	protected static Maze[] mazes=new Maze[NUM_MAZES];			
	
	//Variables (game state):
	protected BitSet pills,powerPills;
	//level-specific
	protected int curMaze,totLevel,levelTime,totalTime,score, enemyEatMultiplier;
	protected boolean gameOver;
	//hero-specific
	protected Node curHeroLoc;
	protected int lastHeroDir,livesRemaining;
	protected boolean extraLife;

	//enemy-specific
	protected Node[] curEnemyLocs;
	protected int[] lastHeroDirs,edibleTimes,lairTimes;
	
	/////////////////////////////////////////////////////////////////////////////
	/////////////////  Constructors and Initialisers   //////////////////////////
	/////////////////////////////////////////////////////////////////////////////
	
	//Constructor
	protected G(){}

	//loads the mazes and store them
	protected void init()
	{		
		for(int i=0;i<mazes.length;i++)
			if(mazes[i]==null)
				mazes[i]=new Maze(i);		
	}
	
	//Creates an exact copy of the game
	public Game copy()
	{
		G copy=new G();
		copy.pills=(BitSet)pills.clone();
		copy.powerPills=(BitSet)powerPills.clone();		
		copy.curMaze=curMaze;
		copy.totLevel=totLevel;
		copy.levelTime=levelTime;
		copy.totalTime=totalTime;
		copy.score=score;
		copy.enemyEatMultiplier = enemyEatMultiplier;
		copy.gameOver=gameOver;
		copy.curHeroLoc = curHeroLoc;
		copy.lastHeroDir = lastHeroDir;
		copy.livesRemaining=livesRemaining;
		copy.extraLife=extraLife;
		copy.curEnemyLocs =Arrays.copyOf(curEnemyLocs, curEnemyLocs.length);
		copy.lastHeroDirs =Arrays.copyOf(lastHeroDirs, lastHeroDirs.length);
		copy.edibleTimes=Arrays.copyOf(edibleTimes,edibleTimes.length);
		copy.lairTimes=Arrays.copyOf(lairTimes,lairTimes.length);
		
		return copy;
	}
	
	//If the hero has been eaten or a new level has been reached
	protected void reset(boolean newLevel)
	{
		if(newLevel)
		{
			curMaze=(curMaze+1)%G.NUM_MAZES;
			totLevel++;
			levelTime=0;
			pills.set(0,getNumberPills());
			powerPills.set(0,getNumberPowerPills());						
		}
		
		curHeroLoc = getInitialHeroPosition();
		lastHeroDir = G.INITIAL_HERO_DIR;
		
		Arrays.fill(curEnemyLocs,mazes[curMaze].lairPosition);
		lastHeroDirs =Arrays.copyOf(G.INITIAL_ENEMY_DIRS,G.INITIAL_ENEMY_DIRS.length);
	
		Arrays.fill(edibleTimes,0);		
		enemyEatMultiplier =1;
		
		for(int i=0;i<lairTimes.length;i++)
			lairTimes[i]=(int)(G.LAIR_TIMES[i]*(Math.pow(LAIR_REDUCTION,totLevel)));
	}
		
	/////////////////////////////////////////////////////////////////////////////
	/////////////////////////////  Game Play   //////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////
			
	//Central method that advances the game state
	public int[] advanceGame(int heroDir, int[] enemyDirs)
	{			
		updateHero(heroDir);			//move the hero
		eatPill();							//eat a pill
		boolean reverse=eatPowerPill();		//eat a power pill
		updateEnemies(enemyDirs,reverse);	//move enemies
		
		//This is primarily done for the replays as reset (as possibly called by feast()) sets the 
		//last directions to the initial ones, not the ones taken
		int[] actionsTakens={lastHeroDir, lastHeroDirs[0], lastHeroDirs[1], lastHeroDirs[2], lastHeroDirs[3]};
		
		feast();							//enemies eat the hero or vice versa
		
		for(int i=0;i<lairTimes.length;i++)
			if(lairTimes[i]>0)
			{
				lairTimes[i]--;
			
				if(lairTimes[i]==0)
					curEnemyLocs[i] = mazes[curMaze].initialEnemiesPosition;
			}
				
		if(!extraLife && score>=EXTRA_LIFE_SCORE)	//award 1 extra life at 10000 points
		{
			extraLife=true;
			livesRemaining++;
		}
	
		totalTime++;
		levelTime++;
		checkLevelState();	//check if level/game is over
		
		return actionsTakens;
	}
	
	//Updates the location of the hero
	protected void updateHero(int direction)
	{
		direction= checkHeroDir(direction);
		lastHeroDir =direction;
		curHeroLoc =getNeighbor(curHeroLoc,direction);
	}
		
	//Checks the direction supplied by the controller and substitutes for a legal one if necessary
	protected int checkHeroDir(int direction)
	{
		Node[] neighbors= getHeroNeighbors();
				
		if((direction>3 || direction<0 || neighbors[direction] == null) && (lastHeroDir >3 || lastHeroDir <0 || neighbors[lastHeroDir] == null))
			return 4;
		
		if(direction<0 || direction>3)
			direction= lastHeroDir;
		
		if(neighbors[direction] == null)
			if(neighbors[lastHeroDir] != null)
				direction= lastHeroDir;
			else
			{
				int[] options= getPossibleHeroDirs(true);
				direction=options[G.rnd.nextInt(options.length)];
			}

		return direction;		
	}
	
	//Updates the locations of the enemies
	protected void updateEnemies(int[] directions, boolean reverse)
	{
		if(directions==null)
			directions=Arrays.copyOf(lastHeroDirs, lastHeroDirs.length);
		
		for(int i=0;i<directions.length;i++)
		{											
			if(reverse && lairTimes[i]==0)
			{
				lastHeroDirs[i]=getReverse(lastHeroDirs[i]);
				curEnemyLocs[i]=getNeighbor(curEnemyLocs[i], lastHeroDirs[i]);
			}
			else if(lairTimes[i]==0 && (edibleTimes[i]==0 || edibleTimes[i]% ENEMY_SPEED_REDUCTION !=0))
			{
				directions[i]= checkEnemyDir(i,directions[i]);
				lastHeroDirs[i]=directions[i];
				curEnemyLocs[i]=getNeighbor(curEnemyLocs[i],directions[i]);
			}
		}		
	}
	
	//Checks the directions supplied by the controller and substitutes for a legal ones if necessary
	protected int checkEnemyDir(int whichEnemy, int direction)
	{
		if(direction<0 || direction>3)
			direction= lastHeroDirs[whichEnemy];
			
		Node[] neighbors= getEnemyNeighbors(whichEnemy);
			
		if(neighbors[direction] == null)
		{
			if(neighbors[lastHeroDirs[whichEnemy]] != null)
				direction= lastHeroDirs[whichEnemy];
			else
			{
				int[] options= getPossibleEnemyDirs(whichEnemy);
				direction=options[G.rnd.nextInt(options.length)];
			}
		}

		return direction;
	}
		
	//Eats a pill
	protected void eatPill()
	{
		int pillIndex = getPillIndex(curHeroLoc);

		if(pillIndex>=0 && pills.get(pillIndex))
		{
			score+=G.PILL;
			pills.clear(pillIndex);
		}
	}
	
	//Eats a power pill - turns enemies edible (blue)
	protected boolean eatPowerPill()
	{
		boolean reverse=false;
		int powerPillIndex=getPowerPillIndex(curHeroLoc);
		
		if(powerPillIndex>=0 && powerPills.get(powerPillIndex))
		{
			score+=G.POWER_PILL;
			enemyEatMultiplier =1;
			powerPills.clear(powerPillIndex);
			
			//This ensures that only enemies outside the lair (i.e., inside the maze) turn edible
			int newEdibleTime=(int)(G.EDIBLE_TIME*(Math.pow(G.EDIBLE_TIME_REDUCTION,totLevel)));
			
			for(int i = 0; i< NUM_ENEMY; i++)
				if(lairTimes[i]==0)
					edibleTimes[i]=newEdibleTime;
				else
					edibleTimes[i]=0;
			
			//This turns all enemies edible, independent on whether they are in the lair or not
//			Arrays.fill(edibleTimes,(int)(G.EDIBLE_TIME*(Math.pow(G.EDIBLE_TIME_REDUCTION,totLevel))));						
			
			reverse=true;
		}
		else if(levelTime>1 && G.rnd.nextDouble()<G.ENEMY_REVERSAL)	//random enemy reversal
			reverse=true;
		
		return reverse;
	}
	
	//This is where the characters of the game eat one another if possible
	protected void feast()
	{		
		for(int i = 0; i< curEnemyLocs.length; i++)
		{
			int distance=getPathDistance(curHeroLoc, curEnemyLocs[i]);
			
			if(distance<=G.EAT_DISTANCE && distance!=-1)
			{
				if(edibleTimes[i]>0)									//hero eats enemy
				{
					score+=G.ENEMY_EAT_SCORE * enemyEatMultiplier;
					enemyEatMultiplier *=2;
					edibleTimes[i]=0;					
					lairTimes[i]=(int)(G.COMMON_LAIR_TIME*(Math.pow(G.LAIR_REDUCTION,totLevel)));					
					curEnemyLocs[i] = mazes[curMaze].lairPosition;
					lastHeroDirs[i] = G.INITIAL_ENEMY_DIRS[i];
				}
				else													//enemy eats hero
				{
					livesRemaining--;
					
					if(livesRemaining<=0)
					{
						gameOver=true;
						return;
					}
					else
						reset(false);
				}
			}
		}
		
		for(int i=0;i<edibleTimes.length;i++)
			if(edibleTimes[i]>0)
				edibleTimes[i]--;
	}
	
	//Checks the state of the level/game and advances to the next level or terminates the game
	protected void checkLevelState()
	{
		//if all pills have been eaten or the time is up...
		if((pills.isEmpty() && powerPills.isEmpty()) || levelTime>=LEVEL_LIMIT)
		{
			//award any remaining pills to the hero
			score+=G.PILL*pills.cardinality()+G.POWER_PILL*powerPills.cardinality();			 
			
			//put a cap on the total number of levels played
			if(totLevel+1==G.MAX_LEVELS)
			{
				gameOver=true;
				return;
			}
			else
				reset(true);
		}		
	}
	
	/////////////////////////////////////////////////////////////////////////////
	///////////////////////////  Getter Methods  ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////
		
	//Returns the reverse of the direction supplied
	public int getReverse(int direction)
	{
		switch(direction)
		{
			case 0: return 2;
			case 1: return 3;
			case 2: return 0;
			case 3: return 1;
		}
		
		return 4;
	}
	
	//Whether the game is over or not
	public boolean gameOver()
	{
		return gameOver;
	}
	
	//Whether the pill specified is still there
	public boolean checkPill(int nodeIndex)
	{
		return pills.get(nodeIndex);
	}
	
	//Whether the power pill specified is still there
	public boolean checkPowerPill(int nodeIndex)
	{
		return powerPills.get(nodeIndex);
	}
	
	//Returns the neighbours of the node at which the hero currently resides
	public Node[] getHeroNeighbors()
	{
		return Arrays.copyOf(curHeroLoc.neighbors, curHeroLoc.neighbors.length);
	}
	
	//Returns the neighbours of the node at which the specified enemy currently resides. NOTE: since enemies are not allowed to reverse, that
	//neighbour is filtered out. Alternatively use: getNeighbour(), given curEnemyLoc[-] for all directions
	public Node[] getEnemyNeighbors(int whichEnemy)
	{
		Node[] neighbors=Arrays.copyOf(curEnemyLocs[whichEnemy].neighbors, curEnemyLocs[whichEnemy].neighbors.length);
		neighbors[getReverse(lastHeroDirs[whichEnemy])] = null;
		
		return neighbors;
	}
	
	//The current level
	public int getCurLevel()
	{
		return totLevel;
	}
	
	//The current maze (1-4)
	public int getCurMaze()
	{
		return curMaze;
	}
	
	//Current node index of the hero
	public Node getCurHeroLoc()
	{
		return curHeroLoc;
	}
	
	//Current node index of the hero
	public int getCurHeroDir()
	{
		return lastHeroDir;
	}
	
	//Lives that remain for the hero
	public int getLivesRemaining()
	{
		return livesRemaining;
	}
	
	//Current node at which the specified enemy resides
	public Node getCurEnemyLoc(int whichEnemy)
	{
		return curEnemyLocs[whichEnemy];
	}

	//Current direction of the specified enemy
	public int getCurEnemyDir(int whichEnemy)
	{
		return lastHeroDirs[whichEnemy];
	}
	
	//Returns the edible time for the specified enemy
	public int getEdibleTime(int whichEnemy)
	{
		return edibleTimes[whichEnemy];
	}
	
	//Simpler check to see if a enemy is edible
	public boolean isEdible(int whichEnemy)
	{
		return edibleTimes[whichEnemy]>0;
	}

	//Returns the score of the game
	public int getScore()
	{
		return score;
	}
	
	//Returns the time of the current level (important with respect to LEVEL_LIMIT)
	public int getLevelTime()
	{
		return levelTime;
	}
	
	//Total time the game has been played for (at most LEVEL_LIMIT*MAX_LEVELS)
	public int getTotalTime()
	{
		return totalTime;
	}
	
	//Total number of pills in the maze
	public int getNumberPills()
	{
		return mazes[curMaze].pillNodes.length;
	}
	
	//Total number of power pills in the maze
	public int getNumberPowerPills()
	{
		return mazes[curMaze].powerPillNodes.length;
	}
	
	//Time left that the specified enemy will spend in the lair
	public int getLairTime(int whichEnemy)
	{
		return lairTimes[whichEnemy];
	}
	
	//If in lair (getLairTime(-)>0) or if not at junction
	public boolean enemyRequiresAction(int whichEnemy)
	{
		return (isJunction(curEnemyLocs[whichEnemy]) && (edibleTimes[whichEnemy]==0 || edibleTimes[whichEnemy]% ENEMY_SPEED_REDUCTION !=0));
	}
	
	//Returns name of maze: A, B, C, D
	public String getName()
	{
		return mazes[curMaze].name;
	}
				
	//Returns the starting position of the hero
	public Node getInitialHeroPosition()
	{
		return mazes[curMaze].initialHeroPosition;
	}
	
	//Returns the starting position of the enemies (i.e., first node AFTER leaving the lair)
	public Node getInitialEnemiesPosition()
	{
		return mazes[curMaze].initialEnemiesPosition;
	}
	
	//Total number of nodes in the graph (i.e., those with pills, power pills and those that are empty)
	public int getNumberOfNodes()
	{
		return mazes[curMaze].graph.length;
	}

	//Returns the pill index of the node. If it is -1, the node has no pill. Otherwise one can
	//use the bitset to check whether the pill has already been eaten
	public int getPillIndex(Node node)
	{
		return node.getPillIndex();
	}
	
	//Returns the power pill index of the node. If it is -1, the node has no pill. Otherwise one 
	//can use the bitset to check whether the pill has already been eaten
	public int getPowerPillIndex(Node node)
	{
		return node.getPowerPillIndex();
	}
	
	//Returns the neighbour of node index that corresponds to direction. In the case of neutral, the 
	//same node index is returned
	public Node getNeighbor(Node startNode, int direction)
	{
		if(direction<0 || direction>3)//this takes care of "neutral"
			return startNode;
		else
			return startNode.neighbors[direction];
	}
		
	//Returns the indices to all the nodes that have pills
	public Node[] getPillNodes()
	{
		return Arrays.copyOf(mazes[curMaze].pillNodes, mazes[curMaze].pillNodes.length);
	}

	//Returns all the nodes that have power pills
	public Node[] getPowerPillNodes()
	{
		return Arrays.copyOf(mazes[curMaze].powerPillNodes, mazes[curMaze].powerPillNodes.length);
	}

	//Returns the indices to all the nodes that are junctions
	public Node[] getJunctionNodes()
	{
		return Arrays.copyOf(mazes[curMaze].junctionNodes, mazes[curMaze].junctionNodes.length);
	}

	//Checks of a node is a junction
	public boolean isJunction(Node node)
	{
		return node.getNumNeighbors() > 2;
	}
	
	//Returns the number of neighbours of a node: 2, 3 or 4. Exception: lair, which has no neighbours
	public int getNumNeighbors(Node node)
	{
		return node.getNumNeighbors();
	}
	
	//Returns the actual directions the hero can take
	public int[] getPossibleHeroDirs(boolean includeReverse)
	{
		return getPossibleDirs(curHeroLoc, lastHeroDir,includeReverse);
	}
	
	//Returns the actual directions the specified enemy can take
	public int[] getPossibleEnemyDirs(int whichEnemy)
	{
		return getPossibleDirs(curEnemyLocs[whichEnemy], lastHeroDirs[whichEnemy],false);
	}
	
	//Computes the directions to be taken given the current location
	private int[] getPossibleDirs(Node curLoc, int curDir, boolean includeReverse)
	{
		int numNeighbors = curLoc.getNumNeighbors();

		if(numNeighbors == 0)
			return new int[0];
		
		Node[] nodes = curLoc.neighbors;
		int[] directions;
		
		if(includeReverse || (curDir < 0 || curDir > 3))
			directions=new int[numNeighbors];
		else
			directions=new int[numNeighbors - 1];
		
		int index = 0;
		
		for(int i = 0; i < nodes.length; i++)
			if(nodes[i] != null)
			{
				if(includeReverse || (curDir < 0 || curDir > 3))
					directions[index++] = i;
				else if(i != getReverse(curDir))
					directions[index++] = i;
			}

		return directions;
	}
			
	//Returns the direction the hero should take to approach/retreat a target (to) given some distance
	//measure
	public int getNextHeroDir(Node to, boolean closer, DM measure)
	{
		return getNextDir(curHeroLoc.neighbors, to, closer,measure);
	}
	
	//Returns the direction the enemy should take to approach/retreat a target (to) given some distance
	//measure. Reversals are filtered.
	public int getNextEnemyDir(int whichEnemy, Node to, boolean closer, Game.DM measure)
	{	
		return getNextDir(getEnemyNeighbors(whichEnemy),to,closer,measure);
	}
			
	
	//This method returns the direction to take given some options (usually corresponding to the
	//neighbours of the node in question), moving either towards or away (closer in {true, false})
	//using one of the three distance measures.
	private int getNextDir(Node[] from, Node to, boolean closer, Game.DM measure)
	{
		int dir=-1;

		double min = Integer.MAX_VALUE;
		double max = -Integer.MAX_VALUE;
			
		for(int i = 0; i < from.length; i++)
		{
			if(from[i] != null)
			{
				double dist = 0;
					
				switch(measure)
				{
					case PATH: dist = getPathDistance(from[i], to); break;
					case EUCLID: dist = getEuclideanDistance(from[i] ,to); break;
					case MANHATTEN: dist = getManhattanDistance(from[i], to); break;
				}
					
				if(closer && dist < min)
				{
					min = dist;
					dir = i;
				}
				
				if(!closer && dist > max)
				{
					max = dist;
					dir = i;
				}
			}
		}
		
		return dir;
	}
	
	//Returns the PATH distance from any node to any other node
	public int getPathDistance(Node from, Node to)
	{
		return mazes[curMaze].distances.get(from, to);
	}
	
	//Returns the EUCLEDIAN distance between two nodes in the current maze.
	public double getEuclideanDistance(Node from, Node to)
	{
		return Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
	}
	
	//Returns the MANHATTEN distance between two nodes in the current maze.
	public int getManhattanDistance(Node from, Node to)
	{
		return (int)(Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()));
	}
	
	//Returns the path of adjacent nodes from one node to another, including these nodes
	//E.g., path from a to c might be [a,f,r,t,c]
	public Node[] getPath(Node from, Node to)
	{
		Node currentNode = from;
		ArrayList<Node> path=new ArrayList<Node>();
		int lastDir;

		while(currentNode != to)
		{
			path.add(currentNode);
			Node[] neighbors = currentNode.neighbors;
			lastDir = getNextDir(neighbors, to, true, G.DM.PATH);
			currentNode = neighbors[lastDir];
		}

		Node[] arrayPath=new Node[path.size()];

		for(int i=0; i<arrayPath.length; i++)
			arrayPath[i] = path.get(i);

		return arrayPath;
	}
	
	//Similar to getPath(-) but takes into consideration the fact that enemies may not reverse. Hence the path to be taken
	//may be significantly longer than the shortest available path
	public Node[] getEnemyPath(int whichEnemy, Node to)
	{
		if(curEnemyLocs[whichEnemy].getNumNeighbors()==0)
			return new Node[0];

		Node currentNode = curEnemyLocs[whichEnemy];
		ArrayList<Node> path=new ArrayList<Node>();
		int lastDir= lastHeroDirs[whichEnemy];

		while(currentNode != to)
		{
			path.add(currentNode);
			Node[] neighbours = getEnemyNeighbors(currentNode, lastDir);
			lastDir=getNextDir(neighbours,to,true,G.DM.PATH);
			currentNode = neighbours[lastDir];
		}

		Node[] arrayPath = new Node[path.size()];

		for(int i=0;i<arrayPath.length;i++)
			arrayPath[i]=path.get(i);

		return arrayPath;
	}
	
	//Returns the node from 'targets' that is closest/farthest from the node 'from' given the distance measure specified
	public Node getTarget(Node from, Node[] targets, boolean nearest, Game.DM measure)
	{
		Node target = null;

		double min = Integer.MAX_VALUE;
		double max = -Integer.MAX_VALUE;
		
		for(int i=0;i<targets.length;i++)
		{				
			double dist = 0;
			
			switch(measure)
			{
				case PATH: dist = getPathDistance(targets[i],from); break;
				case EUCLID: dist = getEuclideanDistance(targets[i],from); break;
				case MANHATTEN: dist = getManhattanDistance(targets[i],from); break;
			}
					
			if(nearest && dist < min)
			{
				min = dist;
				target = targets[i];
			}
				
			if(!nearest && dist > max)
			{
				max = dist;
				target = targets[i];
			}
		}
		
		return target;
	}
	
	//Returns the target closes from the position of the enemy, considering that reversals are not allowed
	public Node getEnemyTarget(int whichEnemy, Node[] targets, boolean nearest)
	{
		Node target = null;

		double min=Integer.MAX_VALUE;
		double max=-Integer.MAX_VALUE;
		
		for(int i=0;i<targets.length;i++)
		{				
			double dist= getEnemyPathDistance(whichEnemy,targets[i]);
					
			if(nearest && dist<min)
			{
				min=dist;
				target=targets[i];	
			}
				
			if(!nearest && dist>max)
			{
				max=dist;
				target=targets[i];
			}
		}
		
		return target;
	}
	
	//Returns the path distance for a particular enemy: takes into account the fact that enemies may not reverse
	public int getEnemyPathDistance(int whichEnemy, Node to)
	{
		return getEnemyPath(whichEnemy, to).length;
	}
	
	//Returns the neighbours of a node with the one correspodining to the reverse of direction being deleted (i.e., =-1)
	private Node[] getEnemyNeighbors(Node node, int lastDirection)
	{
		Node[] neighbors=Arrays.copyOf(node.neighbors, node.neighbors.length);
		neighbors[getReverse(lastDirection)] = null;
		
		return neighbors;
	}		
	
	/*
	 * Stores the actual mazes, each of which is simply a connected graph. The differences between the mazes are the connectivity
	 * and the x,y coordinates (used for drawing or to compute the Euclidean distance. There are 3 built-in distance functions in
	 * total: Euclidean, Manhatten and Dijkstra's shortest path distance. The latter is pre-computed and loaded, the others are
	 * computed on the fly whenever getNextDir(-) is called.
	 */
	protected final class Maze
	{
		protected DualMap<Node, Node, Integer> distances;
		protected Node[] pillNodes, powerPillNodes, junctionNodes;
		protected Node[] graph;
//		protected int[] distances, pillIndices, powerPillIndices, junctionIndices;				//Information for the controllers

		//The actual maze, stored as a graph (set of nodes)
		protected Node initialHeroPosition, lairPosition, initialEnemiesPosition;
		protected int width, height;	//Maze-specific information
		protected String name;																//Name of the Maze
					
		/*
		 * Each maze is stored as a (connected) graph: all nodes have neighbours, stored in an array of length 4. The
		 * index of the array associates the direction the neighbour is located at: '[up,right,down,left]'.
		 * For instance, if node '9' has neighbours '[-1,12,-1,6]', you can reach node '12' by going right, and node
		 * 6 by going left. The directions returned by the controllers should thus be in {0,1,2,3} and can be used
		 * directly to determine the next node to go to.
		 */		
		protected Maze(int index)
		{
			loadNodes(nodeNames[index]);
			loadDistances(distNames[index]);
		}
		
		//Loads all the nodes from files and initialises all maze-specific information.
		private void loadNodes(String fileName)
		{
		    final int NODE_LENGTH = 9;
	        try
	        {
	            // Prepare a stream to read data from the file.
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(G.pathMazes + System.getProperty("file.separator") + fileName)));
                LinkedList<int[]> nodeData = new LinkedList<int[]>();

                // First, read in the name of this map.
                String input = reader.readLine();
                this.name = input.substring(0, input.indexOf("\t"));
                input = input.substring(input.indexOf("\t") + 1);

                // Then, read in the rest of the information (all integers.)
                do
                {
                    String[] tokens = input.split("\t");
                    int[] entry = new int[tokens.length];

                    for (int index = 0; index < tokens.length; index++)
                        entry[index] = Integer.parseInt(tokens[index]);

                    nodeData.add(entry);
                    input = reader.readLine();
                }
                while (input != null);

                // Load general map information
                int[] preamble = nodeData.remove();
				int initialHeroIndex = preamble[0];
				int lairIndex = preamble[1];
				int initialEnemiesIndex = preamble[2];

                this.graph = new Node[preamble[3]];
                this.pillNodes = new Node[preamble[4]];
                this.powerPillNodes = new Node[preamble[5]];
                this.junctionNodes = new Node[preamble[6]];
                this.width = preamble[7];
                this.height = preamble[8];

	            int nodeIndex=0;
	        	    int pillIndex=0;
	        	    int powerPillIndex=0;
    	            	int junctionIndex=0;

                // Create the nodes.
                for (int[] entry : nodeData)
                {
                    Node node = new Node(entry[1], entry[2], entry[7], entry[8]);

                    graph[nodeIndex++] = node;

                    if (node.getPillIndex() >= 0)
                        pillNodes[pillIndex++] = node;
                    else if (node.getPowerPillIndex() >= 0)
                        powerPillNodes[powerPillIndex++] = node;

                    if (node.getNumNeighbors() > 2)
                        junctionNodes[junctionIndex++] = node;
                }

                // Connect the nodes.
                for (int index = 0; index < graph.length; index++)
                {
                    Node[] newNeighbors = new Node[4];
                    for (int neighborNo = 0; neighborNo < 4; neighborNo++)
                    {
                        int neighborIndex = nodeData.get(index)[neighborNo+3];
                        newNeighbors[neighborNo] = (neighborIndex == -1 ? null : graph[neighborIndex]);
                    }
                    graph[index].setNeighbors(newNeighbors);
                }

                // Set up the starting positions.
				this.initialHeroPosition = graph[initialHeroIndex];
				this.lairPosition = graph[lairIndex];
				this.initialEnemiesPosition = graph[initialEnemiesIndex];
	        }
	        catch(IOException ioe)
	        {
	            ioe.printStackTrace();
	        }
		}
		
		/*
		 * Loads the shortest path distances which have been pre-computed. The data contains the shortest distance from
		 * any node in the maze to any other node. Since the graph is symmetric, the symmetries have been removed to preserve
		 * memory and all distances are stored in a 1D array; they are looked-up using getDistance(-). 
		 */		
		private void loadDistances(String fileName)
		{
			distances = new DualMap<Node, Node, Integer>();

	        try
	        {	        	
	        	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(G.pathMazes+System.getProperty("file.separator")+fileName)));

	            for (int end = 0; end < graph.length; end++)
				{
					for (int start = 0; start <= end; start++)
					{
						String input=br.readLine();
						if (input == null)
							break;

						distances.put(graph[start], graph[end], Integer.parseInt(input));
						distances.put(graph[end], graph[start], Integer.parseInt(input));
					}
				}
	        }
	        catch(IOException ioe)
	        {
	            ioe.printStackTrace();
	        }
		}
	}
	
	/*
	 * Stores all information relating to a node in the graph, including all the indices required
	 * to check and update the current state of the game.
	 */
/*	protected final class Node
	{
		protected int x,y,nodeIndex,pillIndex,powerPillIndex,numNeighbours;
		protected int[] neighbours;
		
		protected Node(String nodeIndex,String x,String y,String pillIndex,String powerPillIndex,String[] neighbours)
		{
			this.nodeIndex=Integer.parseInt(nodeIndex);
			this.x=Integer.parseInt(x);
			this.y=Integer.parseInt(y);
			this.pillIndex=Integer.parseInt(pillIndex);
			this.powerPillIndex=Integer.parseInt(powerPillIndex);		
			
			this.neighbours=new int[neighbours.length];
			
			for(int i=0;i<neighbours.length;i++)
			{
				this.neighbours[i]=Integer.parseInt(neighbours[i]);
			
				if(this.neighbours[i]!=-1)
					numNeighbours++;
			}
		}
	}*/
}