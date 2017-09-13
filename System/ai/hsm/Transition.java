package ai.hsm;

import java.util.LinkedList;

public class Transition {
	public State source;
	public State target;
	public Condition condition;
	public Action action;
	
	public Transition(Action action, Condition condition) {
		this.action = action;
		this.condition = condition;
	}
	
	public Transition() {}

	public boolean isTriggered() {
		return condition.test();
	}

	public int getLevel() {
		return source.getDepth() - target.getDepth();
	}

	public State getTargetState() {
		return target;
	}

	public LinkedList<Action> getAction() {
		LinkedList<Action> result = new LinkedList<Action>();
		result.add(action);
		return result;
	}

}
