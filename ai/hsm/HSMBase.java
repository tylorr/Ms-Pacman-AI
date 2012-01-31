package ai.hsm;

import java.util.LinkedList;

public abstract class HSMBase {
	public final class UpdateResult {
		public LinkedList<Action> actions = new LinkedList<Action>();
		public Transition transition;
		public int level;
	}
	
	public LinkedList<Action> getAction() {
		return new LinkedList<Action>();
	}
	
	public UpdateResult update() {
		UpdateResult result = new UpdateResult();
		result.actions = getAction();
		result.transition = null;
		result.level = 0;
		return result;
	}
	
	public abstract LinkedList<State> getStates();
}
