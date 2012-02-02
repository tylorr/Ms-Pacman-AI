package ai.behaviourtree;

import java.util.LinkedList;

public abstract class Task {
	public LinkedList<Task> children = new LinkedList<Task>();
	
	public abstract boolean run();
}
