/**
 * 
 */
package game.entries.ghosts;

import game.entries.ghosts.*;

/**
 * @author user
 *
 */





public abstract class DecisionTree extends DecisionNode{
  Action run = new RunAwayAction();
  Action chase = new ChaseAction();
}
