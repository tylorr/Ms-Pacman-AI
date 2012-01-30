package ai.fsm;

public class StateMachine {
	
	public StateMachineState initialState;
	
	public StateMachineState currentState;
	
	@SuppressWarnings("unused")
	public Action update() {
		Action actions = null;

        // First case - we have no current state.
        if (currentState == null)
        {
            // In this case we use the entry action for the initial state.
            if (initialState != null) {

                // Transition to the first state
                currentState = initialState;

                // Returns the initial states entry actions
                actions = currentState.getEntryActions();

            } else {

                // We have nothing to do
                actions = null;
            }
        }

        // Otherwise we have a current state to work with
        else {
            // Start off with no transition
            Transition transition = null;

            // Check through each transition in the current state.
            BaseTransition testTransition = currentState.firstTransition;
            while (testTransition != null) {
                if (testTransition.isTriggered()) {
                    transition = (Transition)testTransition;
                    break;
                }
                testTransition = testTransition.next;
            }

            // Check if we found a transition
            if (transition != null) {
                // Find our destination
                StateMachineState nextState = transition.getTargetState();

                // Accumulate our list of actions
                Action tempList = null;
                Action last = null;

                // Add each element to the list in turn
                actions = currentState.getExitActions();
                if (actions != null) {
                	last = actions.getLast();
                }

                tempList = transition.getActions();
                
                if (actions == null) {
                	actions = tempList;
                	last = actions.getLast();
                } else if (tempList != null) {
                	last.next = tempList;
                	last = tempList.getLast();
                }
                
                tempList = nextState.getEntryActions();
                if (actions == null) {
                	actions = tempList;
                	last = actions.getLast();
                } else if (tempList != null) {
                	last.next = tempList;
                }

                // Update the change of state
                currentState = nextState;
            }

            // Otherwise our actions to perform are simply those for the
            // current state.
            else {

                actions = currentState.getActions();
            }
        }

        return actions;
	}
	
}
