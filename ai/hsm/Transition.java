package ai.hsm;

import java.util.LinkedList;

public class Transition {

	public Transition next;

	public boolean isTriggered() {
		return false;
	}

	public int getLevel() {
		return 0;
	}

	public State getTargetState() {
		return null;
	}

	public LinkedList<Action> getAction() {
		return null;
	}

}
