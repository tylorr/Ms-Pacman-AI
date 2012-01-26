package ai.fsm;

public abstract class BaseTransition {
	
	public abstract boolean isTriggered();
	
	public Action getActions() {
		return null;
	}
	
	public BaseTransition next;
}
