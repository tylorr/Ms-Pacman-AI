/**
 * 
 */
package game.entries.ghosts;

import game.entries.ghosts.*;
/**
 * @author user
 *
 */
public abstract class Decision extends DecisionNode{
	
public DecisionNode trueNode;
public DecisionNode falseNode;
public TestValue testValue;

public abstract DecisionNode getBranch();


public DecisionNode makeDecision(){
	
  DecisionNode branch = getBranch();
  return branch.makeDecision();
  }
}


