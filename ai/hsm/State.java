package ai.hsm;

public class State extends HSMBase {
	Transition firstTransition;
	
	public Action getActions() {
		return null;
	}
	
	public UpdateResult getEntryActions() {
		return null;
	}
	
	public Action getExitActions() {
		return null;
	}

	@Override
	public State getStates() {
		return this;
	}
}
