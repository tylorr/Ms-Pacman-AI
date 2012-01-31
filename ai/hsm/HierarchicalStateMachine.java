package ai.hsm;

public class HierarchicalStateMachine extends HSMBase {
	public State states;
	
	public State initialState;
	public State currentState = null;
	
	@Override
	public State getStates() {
		if (currentState != null) {
			return currentState.getStates();
		}
		return null;
	}
	
	@Override
	public UpdateResult update() {
		if (currentState == null) {
			currentState = initialState;
			return currentState.getEntryActions();
		}
		
		Transition triggeredTransition = null;
		Transition transition = currentState.firstTransition;
		while (transition != null) {
			if (transition.isTriggered()) {
				triggeredTransition = transition;
				break;
			}
			transition = transition.next;
		}
		
		UpdateResult result;
		
		if (triggeredTransition != null) {
			result = new UpdateResult();
			result.actions = null;
			result.transition = triggeredTransition;
			result.level = triggeredTransition.getLevel();
		} else {
			result = currentState.update();
		}
		
		if (result.transition != null) {
			if (result.level == 0) {
				State targetState = result.transition.getTargetState();
				Action last = null;
				Action tempAction;
				
				tempAction = currentState.getExitActions();
				if (result.actions == null) {
					result.actions = tempAction;
					last = result.actions.getLast();
				} else {
					last = result.actions.getLast();
					last.next = tempAction;
					last = tempAction.getLast();
				}
				
				if (result.actions == null) {
					
				}
			}
		}
		return null;
	}
	
}
