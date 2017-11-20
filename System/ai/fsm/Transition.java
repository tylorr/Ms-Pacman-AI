package ai.fsm;

public abstract class Transition extends BaseTransition {
	
	public StateMachineState target;
	
	public Condition condition;
	
	public StateMachineState getTargetState() {
		return target;
	}
	
	public boolean isTriggered() {
		return condition.test();
	}
	
}
