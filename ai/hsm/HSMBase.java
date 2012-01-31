package ai.hsm;

public abstract class HSMBase {
	final class UpdateResult {
		public Action actions;
		public Transition transition;
		public int level;
	}
	
	public Action getAction() {
		return null;
	}
	
	public UpdateResult update() {
		UpdateResult result = new UpdateResult();
		result.actions = getAction();
		result.transition = null;
		result.level = 0;
		return result;
	}
	
	public abstract State getStates();
}
