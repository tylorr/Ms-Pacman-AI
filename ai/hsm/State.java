package ai.hsm;

import java.util.LinkedList;

public class State extends HSMBase {
	LinkedList<Transition> transitions = new LinkedList<Transition>();
	public SubMachineState parent;
	
	public LinkedList<Action> getAction() {
		return new LinkedList<Action>();
	}
	
	public LinkedList<Action> getEntryAction() {
		return new LinkedList<Action>();
	}
	
	public LinkedList<Action> getExitAction() {
		return new LinkedList<Action>();
	}

	@Override
	public LinkedList<State> getStates() {
		LinkedList<State> result = new LinkedList<State>();
		result.add(this);
		return result;
	}
	
	public int getDepth() {
		if (parent == null) {
			return 0;
		}
		
		return 1 + parent.getDepth();
	}
	
	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
}
