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
package game.system;
import game.models.*;

import java.util.Random;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/*
 * Simple implementation of the game. The class Game contains all code relating to the
 * game; the class GameView displays the game. Controllers must implement HeroController
 * and EnemyController respectively. The game may be executed using Exec.
 */
public class _Game implements Game
{	
	//File names for data
	public static String[] nodeNames = {"a","b","c","d"};
	public static String[] distNames = {"da","db","dc","dd"};
	public static String pathMazes = "data";
	
	//Static stuff (mazes are immutable - hence static)
	protected static _Maze[] mazes = new _Maze[NUM_MAZES];
	
	//Variables (game state):
	HashSet<Node> pills, powerPills;
//	protected BitSet pills,powerPills;

	//level-specific
	protected int curMaze,totLevel,levelTime,totalTime,score, enemyEatMultiplier;
	protected boolean gameOver;

	// Actors
	protected _Hero hero;
	protected _Enemy[] enemies;

	protected int livesRemaining;
	protected boolean extraLife;

	/////////////////////////////////////////////////////////////////////////////
	/////////////////  Constructors and Initializers   //////////////////////////
	/////////////////////////////////////////////////////////////////////////////
	
	//Constructor
	protected _Game(){}

	//loads the mazes and store them
	protected void init()
	{		
		for(int i=0;i<mazes.length;i++)
			if(mazes[i]==null)
				mazes[i]=new _Maze(i);
	}
	
	//Creates an exact copy of the game
	public Game copy()
	{
		_Game copy=new _Game();
		copy.pills= (HashSet<Node>) pills.clone();
		copy.powerPills = (HashSet<Node>) powerPills.clone();
		copy.curMaze=curMaze;
		copy.totLevel=totLevel;
		copy.levelTime=levelTime;
		copy.totalTime=totalTime;
		copy.score=score;
		copy.enemyEatMultiplier = enemyEatMultiplier;
		copy.gameOver=gameOver;
		copy.hero = hero.clone();
		copy.livesRemaining=livesRemaining;
		copy.extraLife=extraLife;
		copy.enemies = new _Enemy[enemies.length];

		for (int index = 0; index < enemies.length; index++)
			copy.enemies[index] = enemies[index].clone();

		return copy;
	}
	
	//If the hero has been eaten or a new level has been reached
	protected void reset(boolean newLevel)
	{
		if(newLevel)
		{
			curMaze=(curMaze+1)% _Game.NUM_MAZES;
			totLevel++;
			levelTime=0;
			pills.addAll(mazes[curMaze].getPillNodes());
			powerPills.addAll(mazes[curMaze].getPowerPillNodes());
		}

		hero = new _Hero(mazes[curMaze].getInitialHeroPosition(), _Game.INITIAL_HERO_DIR);

		for (int index = 0; index < enemies.length; index++)
		{
			enemies[index] = new _Enemy(mazes[curMaze].lairPosition, _Game.INITIAL_ENEMY_DIRS[index], (int)(_Game.LAIR_TIMES[index]*(Math.pow(LAIR_REDUCTION,totLevel))));
		}

		enemyEatMultiplier = 1;
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
		int[] actionsTakens = { hero.direction, enemies[0].direction, enemies[1].direction, enemies[2].direction, enemies[3].direction };
		
		feast();							//enemies eat the hero or vice versa
		
		for(int i = 0; i < enemies.length; i++)
		{
			if (enemies[i].lairTime > 0)
			{
				enemies[i].lairTime--;

				if (enemies[i].lairTime == 0)
					enemies[i].location = mazes[curMaze].initialEnemiesPosition;
			}
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

	public Hero getHero() { return hero.clone(); }
	public Enemy getEnemy(int whichEnemy) { return enemies[whichEnemy].clone(); }

	public List<Enemy> getEnemies()
	{
		ArrayList<Enemy> result = new ArrayList<Enemy>();

		for (_Enemy enemy : enemies)
			result.add(enemy.clone());

		return result;
	}

	//Updates the location of the hero
	protected void updateHero(int direction)
	{
		direction = checkHeroDir(direction);
		hero.direction = direction;
		hero.location = hero.location.getNeighbor(direction);
	}
		
	//Checks the direction supplied by the controller and substitutes for a legal one if necessary
	protected int checkHeroDir(int direction)
	{
		List<Node> neighbors = hero.location.getNeighbors();
		int oldDirection = hero.direction;
				
		if((direction > 3 || direction < 0 || neighbors.get(direction) == null) && (oldDirection > 3 || oldDirection < 0 || neighbors.get(oldDirection) == null))
			return 4;
		
		if(direction < 0 || direction > 3)
			direction = oldDirection;
		
		if(neighbors.get(direction) == null)
			if(neighbors.get(oldDirection) != null)
				direction = oldDirection;
			else
			{
				List<Integer> options = hero.getPossibleDirs(true);
				direction = options.get(Game.rng.nextInt(options.size()));
			}

		return direction;		
	}
	
	//Updates the locations of the enemies
	protected void updateEnemies(int[] directions, boolean reverse)
	{
//		if(directions==null)
//			directions=Arrays.copyOf(lastEnemyDirs, lastEnemyDirs.length);
		
		for(int i = 0; i < enemies.length; i++)
		{											
			if(reverse && enemies[i].lairTime == 0)
			{
				enemies[i].direction = Node.getReverse(enemies[i].direction);
				enemies[i].location = enemies[i].location.getNeighbor(enemies[i].direction);
			}
			else if(enemies[i].lairTime == 0 && (enemies[i].edibleTime == 0 || enemies[i].edibleTime % ENEMY_SPEED_REDUCTION !=0))
			{
				directions[i] = checkEnemyDir(i, directions[i]);
				enemies[i].direction = directions[i];
				enemies[i].location = enemies[i].location.getNeighbor(directions[i]);
			}
		}		
	}
	
	//Checks the directions supplied by the controller and substitutes for a legal ones if necessary
	protected int checkEnemyDir(int whichEnemy, int direction)
	{
		if(direction < 0 || direction > 3)
			direction = enemies[whichEnemy].direction;
			
		List<Node> neighbors = enemies[whichEnemy].getPossibleLocations();
			
		if(neighbors.get(direction) == null)
		{
			if(neighbors.get(enemies[whichEnemy].direction) != null)
				direction = enemies[whichEnemy].direction;
			else
			{
				List<Integer> options = enemies[whichEnemy].getPossibleDirs();
				direction = options.get(Game.rng.nextInt(options.size()));
			}
		}

		return direction;
	}
		
	//Eats a pill
	protected void eatPill()
	{
		if (pills.contains(hero.location))
		{
			score += Game.PILL_SCORE;
			pills.remove(hero.location);
		}
	}
	
	//Eats a power pill - turns enemies edible (blue)
	protected boolean eatPowerPill()
	{
		boolean reverse = false;

		if(powerPills.contains(hero.location))
		{
			score += Game.POWER_PILL_SCORE;
			enemyEatMultiplier =1;
			powerPills.remove(hero.location);
			
			//This ensures that only enemies outside the lair (i.e., inside the maze) turn edible
			int newEdibleTime=(int)(Game.EDIBLE_TIME * (Math.pow(Game.EDIBLE_TIME_REDUCTION, totLevel)));
			
			for(int i = 0; i< NUM_ENEMY; i++)
				if(enemies[i].lairTime == 0)
					enemies[i].edibleTime = newEdibleTime;
				else
					enemies[i].edibleTime = 0;
			
			//This turns all enemies edible, independent on whether they are in the lair or not
//			Arrays.fill(edibleTimes,(int)(_Game.EDIBLE_TIME*(Math.pow(_Game.EDIBLE_TIME_REDUCTION,totLevel))));
			
			reverse = true;
		}
		else if (levelTime > 1 && Game.rng.nextDouble() < Game.ENEMY_REVERSAL)	//random enemy reversal
			reverse=true;
		
		return reverse;
	}
	
	//This is where the characters of the game eat one another if possible
	protected void feast()
	{
		for(int i = 0; i < enemies.length; i++)
		{
			int distance=hero.location.getPathDistance(enemies[i].location);
			
			if(distance <= Game.EAT_DISTANCE && distance != -1)
			{
				if(enemies[i].edibleTime > 0)									//hero eats enemy
				{
					score+= Game.ENEMY_EAT_SCORE * enemyEatMultiplier;
					enemyEatMultiplier *=2;
					enemies[i].edibleTime = 0;
					enemies[i].lairTime = (int)(Game.COMMON_LAIR_TIME*(Math.pow(Game.LAIR_REDUCTION,totLevel)));
					enemies[i].location = mazes[curMaze].lairPosition;
					enemies[i].direction = Game.INITIAL_ENEMY_DIRS[i];
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
		
		for(int i = 0; i < enemies.length;i++)
			if(enemies[i].edibleTime > 0)
				enemies[i].edibleTime--;
	}
	
	//Checks the state of the level/game and advances to the next level or terminates the game
	protected void checkLevelState()
	{
		//if all pills have been eaten or the time is up...
		if((pills.isEmpty() && powerPills.isEmpty()) || levelTime>=LEVEL_LIMIT)
		{
			//award any remaining pills to the hero
			score+= _Game.PILL_SCORE * pills.size() + Game.POWER_PILL_SCORE * powerPills.size();

			//put a cap on the total number of levels played
			if(totLevel+1== _Game.MAX_LEVELS)
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

	//Whether the game is over or not
	public boolean gameOver()
	{
		return gameOver;
	}
	
	//Whether the pill specified is still there
	public boolean checkPill(Node location)
	{
		return pills.contains(location);
	}
	
	//Whether the power pill specified is still there
	public boolean checkPowerPill(Node location)
	{
		return powerPills.contains(location);
	}

	public List<Node> getPillList() { return new ArrayList<Node>(pills); }
	public List<Node> getPowerPillList() { return new ArrayList<Node>(powerPills); }

	//The current level
	public int getCurLevel()
	{
		return totLevel;
	}

	//The current maze # (1-4)
	public int getCurMazeNum()
	{
		return curMaze;
	}

	//The current maze object
	public Maze getCurMaze()
	{
		return mazes[curMaze];
	}

	//Lives that remain for the hero
	public int getLivesRemaining()
	{
		return livesRemaining;
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
	
	//Returns the pill index of the node. If it is -1, the node has no pill. Otherwise one can
	//use the bitset to check whether the pill has already been eaten
//	public int getPillIndex(Node node)
//	{
//		return node.getPillIndex();
//	}
	
	//Returns the power pill index of the node. If it is -1, the node has no pill. Otherwise one 
	//can use the bitset to check whether the pill has already been eaten
//	public int getPowerPillIndex(Node node)
//	{
//		return node.getPowerPillIndex();
//	}
}