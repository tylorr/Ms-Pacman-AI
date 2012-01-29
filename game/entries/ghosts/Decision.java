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

abstract class IsBlueDecision extends Decision{
	
}

abstract class NearPowerPill extends Decision{
	
}

abstract class LessThanDecision extends Decision {
	  public float threshold;

	  public DecisionNode getBranch() {
	    if (this.testValue.getValue() < this.threshold) {
	      return this.trueNode;
	    } else {
	      return this.falseNode;
	    }
	  }
}

