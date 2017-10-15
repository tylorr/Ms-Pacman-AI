package game;

import game.models.Game;
import game.system.*;
import game.view.*;

import game.controllers.*;
import game.controllers.examples.*;

import pakku.agent.TestAgent;

/*
 * This class may be used to execute the game in timed or un-timed modes, with or without
 * visuals. Competitors should implement their controllers in game.entries.ghosts and 
 * game.entries.pacman respectively. The skeleton classes are already provided. The package
 * structure should not be changed (although you may create sub-packages in these packages).
 */
@SuppressWarnings("unused")
public class Exec
{	
	//Several options are listed - simply remove comments to use the option you want
	public static void main(String[] args)
	{
		Exec exec=new Exec();

		HeroController studentPacMan = new TestAgent();
//		HeroController examplePacMan = new pakku.agent.example();
		EnemyController ghosts = new OriginalGhosts();

		if (args.length > 0)
		{
			if (args[0].toLowerCase().equals("-testexample"))
//				exec.runExperiment(examplePacMan, ghosts, 100);
				exec.runExperiment(studentPacMan, ghosts, 100);
			else if (args[0].toLowerCase().equals("-teststudent"))
				exec.runExperiment(studentPacMan, ghosts, 100);
			else if (args[0].toLowerCase().equals("-visualexample"))
//				exec.runGame(examplePacMan, ghosts, true, _Game.DELAY);
				exec.runGame(studentPacMan, ghosts, true, _Game.DELAY);
			else
				exec.runGame(studentPacMan, ghosts, true, _Game.DELAY);
		}
		else
			exec.runGame(studentPacMan, ghosts, true, _Game.DELAY);
		
		//this can be used for numerical testing (non-visual, no delays)
//		exec.runExperiment(new RandomHero(),new AttractRepelGhosts(true),100);
		
		//run game without time limits (un-comment if required)
//		exec.runGame(new RandomHero(),new RandomGhosts(),true,_Game.DELAY);
		
		//run game with time limits (un-comment if required)
//		exec.runGameTimed(new Human(),new AttractRepelGhosts(true),true);

		//run game with time limits. Here NearestPillHeroVS is chosen to illustrate how to use graphics for debugging/information purposes
//		exec.runGameTimed(new NearestPillHeroVS(),new AttractRepelGhosts(false),true);
		
		//this allows you to record a game and replay it later. This could be very useful when
		//running many games in non-visual mode - one can then pick out those that appear irregular
		//and replay them in visual mode to see what is happening.
//		exec.runGameTimedAndRecorded(new RandomHero(),new Legacy2TheReckoning(),true,"human-v-Legacy2.txt");
//		exec.replayGame("human-v-Legacy2.txt");
	}
	
    protected int pacDir;
    protected int[] ghostDirs;
    protected _Game_ game;
    protected PacMan pacMan;
    protected Ghosts ghosts;
    protected boolean pacmanPlayed,ghostsPlayed;
   
    /*
     * For running multiple games without visuals. This is useful to get a good idea of how well a controller plays
     * against a chosen opponent: the random nature of the game means that performance can vary from game to game. 
     * Running many games and looking at the average score (and standard deviation/error) helps to get a better
     * idea of how well the controller is likely to do in the competition.
     */
    public void runExperiment(HeroController heroController, EnemyController enemyController, int trials)
    {
    	double avgScore=0;
    	
		game=new _Game_();
		
		for(int i=0;i<trials;i++)
		{
			game.newGame();
			heroController.init();
			enemyController.init();

			while(!game.gameOver())
			{
				long due=System.currentTimeMillis()+ _Game.DELAY;
				Game state = game.copy();
				enemyController.update(state, due);
				heroController.update(state, due);
		        game.advanceGame(heroController.getAction(), enemyController.getActions());
			}
			
			avgScore+=game.getScore();
			heroController.shutdown();
			enemyController.shutdown();
//			System.out.println(game.getScore());
		}
		
		System.out.println(avgScore/trials);
    }
    
    /*
     * Run game without time limit. Very good for testing as game progresses as soon as the controllers
     * return their action(s). Can be played with and without visual display of game states. The delay
     * is purely for visual purposes (as otherwise the game could be too fast if controllers compute quickly. 
     * For testing, this can be set to 0 for fasted game play.
     */
	public void runGame(HeroController heroController, EnemyController enemyController, boolean visual, int delay)
	{
//		Game.rng = new java.util.Random();
		
		game=new _Game_();
		game.newGame();

		GameView gv=null;
		
		if(visual)
			gv=new GameView(game).showGame();

		heroController.init();
		enemyController.init();

		while(!game.gameOver())
		{
			long due=System.currentTimeMillis()+ Game.DELAY;
			Game state = game.copy();
			enemyController.update(state, due);
			heroController.update(state, due);
			game.advanceGame(heroController.getAction(), enemyController.getActions());

	        try{Thread.sleep(delay);}catch(Exception e){}
	        
	        if(visual)
	        	gv.repaint();
		}

		heroController.init();
		enemyController.init();
	}
	
    /*
     * Run game with time limit. This is how it will be done in the competition. 
     * Can be played with and without visual display of game states.
     */
	public void runGameTimed(HeroController heroController, EnemyController enemyController, boolean visual)
	{
		game=new _Game_();
		game.newGame();
		pacMan=new PacMan(heroController);
		ghosts=new Ghosts(enemyController);
		
		GameView gv=null;
		
		if(visual)
		{
			gv=new GameView(game).showGame();
			
			if(heroController instanceof Human)
				gv.getFrame().addKeyListener((Human) heroController);
		}

		heroController.init();
		enemyController.init();

		while(!game.gameOver())
		{
			pacMan.alert();
			ghosts.alert();

			try
			{
				Thread.sleep(_Game.DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

	        game.advanceGame(pacDir,ghostDirs);	        
	        
	        if(visual)
	        	gv.repaint();
		}
		
		pacMan.kill();
		ghosts.kill();
	}
	
	/*
	 * Runs a game and records all directions taken by all controllers - the data may then be used to replay any game saved using
	 * replayGame(-).
	 */
	public void runGameTimedAndRecorded(HeroController heroController, EnemyController enemyController, boolean visual, String fileName)
	{
		String history="";
		int lastLevel=0;
		boolean firstWrite=false;	//this makes sure the content of any existing files is overwritten
		
		game=new _Game_();
		game.newGame();
		pacMan=new PacMan(heroController);
		ghosts=new Ghosts(enemyController);
		
		GameView gv=null;
		
		if(visual)
		{
			gv=new GameView(game).showGame();
			
			if(heroController instanceof Human)
				gv.getFrame().addKeyListener((Human) heroController);
		}

		heroController.init();
		enemyController.init();

		while(!game.gameOver())
		{
			pacMan.alert();
			ghosts.alert();

			try
			{
				Thread.sleep(_Game.DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

	        int[] actionsTaken=game.advanceGame(pacDir,ghostDirs);	        
	        
	        if(visual)
	        	gv.repaint();
	        
	        history=addActionsToString(history,actionsTaken);
        	
	        //saves actions after every level
        	if(game.getCurLevel()!=lastLevel)
        	{
        		Replay.saveActions(history,fileName,firstWrite);
        		lastLevel=game.getCurLevel();
        		firstWrite=true;
        	}	   
		}

		heroController.shutdown();
		enemyController.shutdown();

		//save the final actions
		Replay.saveActions(history,fileName,firstWrite);
		
		pacMan.kill();
		ghosts.kill();
	}
	
	/*
	 * This is used to replay a recorded game. The controllers are given by the class Replay which may
	 * also be used to load the actions from file.
	 */
	public void replayGame(String fileName)
	{
		_ReplayGame_ game=new _ReplayGame_();
		game.newGame();

		Replay replay=new Replay(fileName);
		HeroController heroController = replay.getPacMan();
		EnemyController enemyController = replay.getGhosts();

		heroController.init();
		enemyController.init();

		GameView gv=new GameView(game).showGame();
		
		while(!game.gameOver())
		{
			Game state = game.copy();
			enemyController.update(state, 0);
			heroController.update(state, 0);
	        game.advanceGame(heroController.getAction(), enemyController.getActions());
	        
	        gv.repaint();
	        
	        try{Thread.sleep(_Game.DELAY);}catch(Exception e){}
		}

		heroController.shutdown();
		enemyController.shutdown();
	}
	
    private String addActionsToString(String history,int[] actionsTaken)
    {
    	history+=(game.getTotalTime()-1)+"\t"+actionsTaken[0]+"\t";

        for (int i = 0; i< _Game.NUM_ENEMY; i++)
        	history+=actionsTaken[i+1]+"\t";

        history+="\n";
        
        return history;
    }
    	
	//sets the latest direction to take for each game step (if controller replies in time)
	public void setGhostDirs(int[] ghostDirs)
	{
		this.ghostDirs=ghostDirs;
		this.ghostsPlayed=true;
	}
	
	//sets the latest direction to take for each game step (if controller replies in time)
	public void setPacDir(int pacDir)
	{
		this.pacDir=pacDir;
		this.pacmanPlayed=true;
	}
	
	/*
	 * Wraps the controller in a thread for the timed execution. This class then updates the
	 * directions for Exec to parse to the game.
	 */
	public class PacMan extends Thread 
	{
	    private HeroController pacMan;
	    private boolean alive;

	    public PacMan(HeroController pacMan)
	    {
	        this.pacMan=pacMan;
	        alive=true;
	        start();
	    }

	    public synchronized void kill() 
	    {
	        alive=false;
	        notify();
	    }
	    
	    public synchronized void alert()
	    {
	        notify();
	    }

	    public void run() 
	    {
	        while(alive) 
	        {
	        	try 
	        	{
	        		synchronized(this)
	        		{
	        			wait();
	                }

					Game state = game.copy();
					pacMan.update(state, System.currentTimeMillis() + Game.DELAY);
	        		setPacDir(pacMan.getAction());
	            } 
	        	catch(InterruptedException e) 
	        	{
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	/*
	 * Wraps the controller in a thread for the timed execution. This class then updates the
	 * directions for Exec to parse to the game.
	 */
	public class Ghosts extends Thread 
	{
		private EnemyController ghosts;
	    private boolean alive;

	    public Ghosts(EnemyController ghosts)
	    {	    	
	    	this.ghosts=ghosts;
	        alive=true;
	        start();
	    }

	    public synchronized void kill() 
	    {
	        alive=false;
	        notify();
	    }

	    public synchronized void alert() 
	    {
	        notify();
	    }
	    
	    public void run() 
	    {
	        while(alive) 
	        {
	        	try 
	        	{
	        		synchronized(this)
	        		{
	        			wait();
	                }

					Game state = game.copy();
					ghosts.update(state, System.currentTimeMillis()+ Game.DELAY);
	        		setGhostDirs(ghosts.getActions());
	            } 
	        	catch(InterruptedException e) 
	        	{
	                e.printStackTrace();
	            }
	        }
	    }
	}
}