package ai.fun.ghosts;

import game.core.*;

public class Action extends DecisionNode{
	public Action makeDecision(Game game){
		return this;
	}
	public int[] execute(Game game){
		int[] result = new int[4];
		result[0] = -1;
		result[1] = -1;
		result[2] = -1;
		result[3] = -1;
		return result;
	}
}
