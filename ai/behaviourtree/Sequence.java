package ai.behaviourtree;

public class Sequence extends Task {

	@Override
	public boolean run() {
		for (Task t : children) {
			if (!t.run()) return false;
		}
		return true;
	}

}
