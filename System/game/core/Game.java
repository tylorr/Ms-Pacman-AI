/*
 * Generalized update based on "Ms Pac-Man versus Ghost Team Competition" by
 * Philipp Rohlfshagen, David Robles and Simon Lucas of the University of Essex.
 * 
 * Generalized version written by Jeremiah Blanchard at the University of Florida (2017).
 *
 * Original code written by Philipp Rohlfshagen, based on earlier implementations of
 * the game by Simon Lucas and David Robles.
 * 
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package game.core;

import java.util.Random;

/*
 * This interface defines the contract between the game engine and the controllers. It provides all
 * the methods a controller may use to (a) query the game state, (b) compute game-related attributes
 * and (c) test moves by using a forward model (i.e., copy() followed by advanceGame()).
 */
public interface Game
{
	//These constants specify the exact nature of the game
	public static final int UP=0;							//direction going up
	public static final int RIGHT=1;						//direction going right
	public static final int DOWN=2;							//direction going down
	public static final int LEFT=3;							//direction going left
	public static final int EMPTY=-1;						//value of an non-existing neighbour
	public static final int PILL=10;						//points for a pill
	public static final int POWER_PILL=50;					//points for a power pill
	public static final int ENEMY_EAT_SCORE =200;			//score for the first enemy eaten (doubles every time for the duration of a single power pill)
	public static final int EDIBLE_TIME=200;				//initial time an enemy is edible for (decreases as level number increases)
	public static final float EDIBLE_TIME_REDUCTION=0.9f;	//reduction factor by which edible time decreases as level number increases
	public static final int[] LAIR_TIMES={40,60,80,100};	//time spend in the lair by each enemy at the start of a level
	public static final int COMMON_LAIR_TIME=40;			//time spend in lair after being eaten
	public static final float LAIR_REDUCTION=0.9f;			//reduction factor by which lair times decrease as level number increases
	public static final int LEVEL_LIMIT=3000;				//time limit for a level
	public static final float ENEMY_REVERSAL =0.0015f;		//probability of a global enemy reversal event
	public static final int MAX_LEVELS=16;					//maximum number of levels played before the end of the game
	public static final int EXTRA_LIFE_SCORE=10000;			//extra life is awarded when this many points have been collected
	public static final int EAT_DISTANCE=2;					//distance in the connected graph considered close enough for an eating event to take place
	public static final int NUM_ENEMY =4;					//number of enemies in the game
	public static final int NUM_MAZES=4;					//number of different mazes in the game
	public static final int DELAY=40;						//delay (in milliseconds) between game advancements						
	public static final int NUM_LIVES=3;					//total number of lives the hero has (current + NUM_LIVES-1 spares)
	public static final int INITIAL_HERO_DIR =3;				//initial direction taken by the hero
	public static final int[] INITIAL_ENEMY_DIRS ={3,1,3,1};	//initial directions for the enemies (after leaving the lair)
	public static final int ENEMY_SPEED_REDUCTION =2;		//difference in speed when enemies are edible (every ENEMY_SPEED_REDUCTION, an enemy remains stationary)

	public Game copy();												//returns an exact copy of the game (forward model)
	public int[] advanceGame(int heroDir, int[] enemyDirs);		//advances the game using the actions (directions) supplied; returns all directions played [Hero, Enemy1, Enemy2, Enemy3, Enemy4]
	public boolean gameOver();										//returns true if the hero has lost all her lives or if MAX_LEVELS has been reached
	public int getScore();											//returns the score of the game
	public int getLevelTime();										//returns the time for which the CURRENT level has been played
	public int getTotalTime();										//returns the time for which the game has been played (across all levels)
	public int getReverse(int direction);							//returns the reverse of the direction supplied
	public int getLivesRemaining();									//returns the number of lives remaining for the hero
	public int getCurLevel();										//returns the current level

	public String getName();										//returns the name of the maze
	public int getCurMaze();										//returns the current maze
	public int getNumberPills();									//returns the total number of pills in this maze (at the beginning of the level)
	public int getNumberPowerPills();								//returns the total number of power pills in this maze (at the beginning of the level)
	public int getNumberOfNodes();									//returns the total number of nodes in the graph (pills, power pills and empty)
	public Node[] getPillNodes();									//returns all nodes with pills
	public Node[] getPowerPillNodes();								//returns all nodes with power pills
	public Node[] getJunctionNodes();								//returns indices to all nodes that are junctions
	public Node getInitialHeroPosition();								//returns the position where the hero starts at the beginning of the level
	public Node getInitialEnemiesPosition();							//returns the position where the enemies starts at the beginning of the level, AFTER leaving the lair

	public boolean checkPill(int pillIndex);						//checks if the pill specified is still available
	public boolean checkPowerPill(int powerPillIndex);				//checks if the power pill specified is still available
	public int getPillIndex(Node node);							//returns the pill index of the node specified (can be used with the bitset for the pills)
	public int getPowerPillIndex(Node node);					//returns the power pill index of the node specified (can be used with the bitset for the power pills)

	public int getNextDir(Node[] options, Node to, boolean closer, DM measure);//returns the direction the agent should take (among options provides) to approach/retreat from the node specified, using the distance measure specified
	public int getNextEnemyDir(int whichEnemy, Node to, boolean closer, DM measure);
	public Node[] getPath(Node from, Node to);							//returns the path from one node to another (e.g., [1,2,5,7,9] for 1 to 9)
	public Node getTarget(Node from, Node[] targets, boolean nearest, DM measure);	//selects a target from 'targets' given current position ('from'), a distance measure and whether it should be the point closest or farthest

	public enum DM{PATH,EUCLID,MANHATTEN};				 			//simple enumeration for use with the direction methods (below)
	public int getPathDistance(Node from, Node to);					//returns the shortest path distance (Dijkstra) from one node to another
	public double getEuclideanDistance(Node from, Node to);			//returns the Euclidean distance between two nodes
	public int getManhattanDistance(Node from, Node to);				//returns the Manhattan distance between two nodes

	public Hero getHero();
    public Enemy getEnemy(int whichEnemy);

	public Node[] getEnemyPath(int whichEnemy, Node to);				//returns the path from one node to another, taking into account that reversals are not possible
	public Node getEnemyTarget(int whichEnemy, Node[] targets, boolean nearest);	//selects a target for an enemy (accounts for the fact that enemies may not reverse)
	public int getEnemyPathDistance(int whichEnemy, Node to);			//returns the distance of a path for the enemy specified (accounts for the fact that enemies may not reverse)
}