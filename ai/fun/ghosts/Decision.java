package ai.fun.ghosts;

import game.core.*;

/**
 * @author user
 *
 */

abstract class DecisionNode {
	public abstract Action makeDecision(Game game); 
}


 public abstract class Decision extends DecisionNode{
	public DecisionNode trueNode;
	public DecisionNode falseNode;
	
	public DecisionNode getBranch(Game game){
		System.out.println("Warning!");
		return null;
	}
		
	


	public Action makeDecision(Game game) {
		  DecisionNode branch = this.getBranch(game);
		  return branch.makeDecision(game); 
	}

 }




