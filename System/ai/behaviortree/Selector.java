package ai.behaviortree;

public class Selector extends Task {

	@Override
	public boolean run() {
		for (Task t : children) {
			if (t.run()) return true;
		}
		return false;
	}

}
