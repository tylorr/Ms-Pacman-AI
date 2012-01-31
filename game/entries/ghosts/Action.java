package game.entries.ghosts;
import game.core.*;
import game.entries.ghosts.*;

public abstract class Action{
	Game pacManGame;
	int pacManDirection = pacManGame.getCurPacManDir();
	Action makeDecision(){
		return this;
	}
}


