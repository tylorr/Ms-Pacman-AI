package game.entries.ghosts;
import game.core.*;

public class RunAwayAction extends Action{
	Game pacManGame;
  //don't aim for any of pac man's neighbor nodes
  //go for the corners if pac man is far away from you
  //blue's original path could be useful in this	
  //search for the closest decision nodes, figure out distance between each
  //pick the one farthest away from Ms. Pac-Man	
	int direction = pacManGame.getCurPacManDir();
}
