/**
 * 
 */
package game.entries.ghosts;

/**
 * @author user
 *
 */

abstract class DecisionNode {
	public DecisionNode makeDecision() {
		if (getBranch()){
			if (trueBranch == null) return null;
		}
	}
}


class Decision extends DecisionNode{
	public DecisionNode trueNode;
	public DecisionNode falseNode;
	public Action trueAction;
	public Action falseAction;
	public TestValue testValue;
	public double threshold;

	public DecisionNode getBranch() {
		return null;
	}


	public DecisionNode makeDecision(){
		
	  DecisionNode branch = getBranch();
	  return branch.makeDecision();
	  }
	}

	class LessThanDecision extends Decision {
		  
		  public DecisionNode getBranch() {
		    if (this.testValue.getValue() < this.threshold) {
		      return this.trueNode;
		    } else {
		      return this.falseNode;
		    }
		  }
	}


