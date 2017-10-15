/*
 * Generalized maze game version adapted and object-oriented model written by
 * Jeremiah Blanchard at the University of Florida (2017).
 *
 * Generalized update based on "Ms Pac-Man versus Ghost Team Competition" by
 * Philipp Rohlfshagen, David Robles and Simon Lucas of the University of Essex.
 * Original code written by Philipp Rohlfshagen, based on earlier implementations of
 * the game by Simon Lucas and David Robles.
 *
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package game.models;
import java.util.Random;
import java.util.List;

/*
 * This interface defines the contract between the game engine and the controllers. It provides all
 * the methods a controller may use to (a) query the game state, (b) compute game-related attributes
 * and (c) test moves by using a forward model (i.e., copy() followed by advanceGame()).
 */
public interface Game
{
	public int getScore();											// Returns the score of the game
	public int getCurLevel();										// Returns the current level
	public int getLevelTime();										// Returns the time for which the CURRENT level has been played
	public int getTotalTime();										// Returns the time for which the game has been played (across all levels)
	public int getLivesRemaining();									// Returns the number of lives remaining for the hero

	public List<Node> getPillList();								// Get a list of all available pills in the current level
	public List<Node> getPowerPillList();							// Get a list of all available power pills in the current level

	public boolean checkPill(Node location);						// Checks if the location specified is a pill / is still available
	public boolean checkPowerPill(Node location);					// Checks if the location specified is a power pill / is still available

	public Hero getHero();											// Returns a copy of the hero object
	public Enemy getEnemy(int whichEnemy);							// Returns a copy of a specific enemy number
	public List<Enemy> getEnemies();								// Returns a copy of the enemy array

	public Game copy();												// Returns an exact copy of the game (forward model)
	public Maze getCurMaze();										// Returns the current maze information
	public static Random rng = new Random(0);					// Random number generator with fixed seed

	public int[] advanceGame(int heroDir, int[] enemyDirs);			// Advances the game using the actions (directions) supplied; returns all directions played [Hero, Enemy1, Enemy2, Enemy3, Enemy4]
	public boolean gameOver();										// Returns true if the hero has lost all her lives or if MAX_LEVELS has been reached

	//These constants specify the exact nature of the game
	public class Direction { public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, EMPTY = -1; }	//directions

	// Points
	public static final int PILL_SCORE = 10;
	public static final int POWER_PILL_SCORE = 50;
	public static final int ENEMY_EAT_SCORE = 200;

	// Timing
	public static final int EDIBLE_TIME = 200;						//initial time an enemy is edible for (decreases as level number increases)
	public static final float EDIBLE_TIME_REDUCTION = 0.9f;			//reduction factor by which edible time decreases as level number increases
	public static final int[] LAIR_TIMES = {40, 60, 80, 100};		//time spend in the lair by each enemy at the start of a level
	public static final int COMMON_LAIR_TIME = 40;					//time spend in lair after being eaten
	public static final float LAIR_REDUCTION = 0.9f;				//reduction factor by which lair times decrease as level number increases
	public static final int LEVEL_LIMIT = 3000;						//time limit for a level
	public static final int DELAY = 40;								//delay (in milliseconds) between game advancements

	// Initial Game State
	public static final int NUM_LIVES = 3;							//total number of lives the hero has (current + NUM_LIVES-1 spares)
	public static final int INITIAL_HERO_DIR = 3;					//initial direction taken by the hero
	public static final int[] INITIAL_ENEMY_DIRS = {3, 1, 3, 1};	//initial directions for the enemies (after leaving the lair)
	public static final int ENEMY_SPEED_REDUCTION = 2;				//difference in speed when enemies are edible (every ENEMY_SPEED_REDUCTION, an enemy remains stationary)

	// Misc. configurations for game
	public static final float ENEMY_REVERSAL = 0.0015f;				//probability of a global enemy reversal event
	public static final int EXTRA_LIFE_SCORE = 10000;				//extra life is awarded when this many points have been collected
	public static final int EAT_DISTANCE = 2;						//distance in the connected graph considered close enough for an eating event to take place
	public static final int NUM_ENEMY = 4;							//number of enemies in the game
	public static final int NUM_MAZES = 4;							//number of different mazes in the game
	public static final int MAX_LEVELS = 16;						//maximum number of levels played before the end of the game

	public enum DM{PATH,EUCLID,MANHATTEN};				 			//simple enumeration for use with the direction methods
}