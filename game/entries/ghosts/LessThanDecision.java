package game.entries.ghosts;
import game.entries.ghosts.*;

public class LessThanDecision extends Decision {
	  public float threshold;

	  public DecisionNode getBranch() {
	    if (this.testValue.getValue() < this.threshold) {
	      return this.trueNode;
	    } else {
	      return this.falseNode;
	    }
	  }
}
