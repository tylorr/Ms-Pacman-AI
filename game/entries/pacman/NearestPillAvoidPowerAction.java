package game.entries.pacman;

import game.core.Game;

class NearestPillAvoidPowerAction extends PacManAction {
	@Override
	public int act(Game game) {
		int current = game.getCurPacManLoc();
		
		//get all active pills
		int[] activePills = game.getPillIndicesActive();
		
		//get all active power pills
		int[] activePowerPills = game.getPowerPillIndicesActive();
		
		// find closest pill
		int target = game.getTarget(current, activePills, true, Game.DM.PATH);
		
		// find path from pacman to pill
		int[] path = game.getPath(current, target);
		
		boolean powerExists = false;
		int powerLocation = -1;
		
		// Check for a power pill in the path
		for (int i = 0; (i < path.length) && !powerExists; i++) {
			for (int j = 0; j < activePowerPills.length; j++) {
				if (path[i] == activePowerPills[j]) {
					powerExists = true;
					powerLocation = activePowerPills[j];
					break;
				}
			}
		}
		
		// if power pill in path
		// move away from power pill
		if (powerExists) {
			return game.getNextPacManDir(powerLocation, false, Game.DM.PATH);
		}
		
		// otherwise, head straight for the pill
		else {
			return game.getNextPacManDir(target, true, Game.DM.PATH);
		}
	}
}