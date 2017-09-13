package ai.fsm;

public class Action {
	
	public Action next;
	
	public final Action getLast() {
		// If we're at the end, then end
        if (next == null) return this;

        // Otherwise find the end iteratively
        Action thisAction = this;
        Action nextAction = next;
        while(nextAction != null) {
        	thisAction = nextAction;
            nextAction = nextAction.next;
        }

        // The final element is in thisAction, so return it
        return thisAction;
	}
	
	public void act() {}
}
