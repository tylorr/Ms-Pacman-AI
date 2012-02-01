package game.entries.pacman;

import game.core.G;
import game.core.Game;

class NearestPillAction extends PacManAction {
	@Override
	public int act(Game game) {
		int current = game.getCurPacManLoc();
		
		//get all active pills
		int[] activePills = game.getPillIndicesActive();
		
		//get all active power pills
		int[] activePowerPills = game.getPowerPillIndicesActive();
		
		//create a target array that includes all ACTIVE pills and power pills
		//int[] targetsArray = new int[activePills.length+activePowerPills.length];
		
		int target = game.getTarget(current, activePills, true, Game.DM.PATH);
		int[] path = game.getPath(current, target);
		
		boolean powerExists = false;
		int powerLocation = -1;
		
		// Check for a power pill in the path
		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < activePowerPills.length; j++) {
				if (path[i] == activePowerPills[j]) {
					powerExists = true;
					powerLocation = activePowerPills[j];
					break;
				}
			}
		}
		
		// move away from power pill
		if (powerExists) {
			return game.getNextPacManDir(powerLocation, false, Game.DM.PATH);
		}
		
		// Head straight for the pill
		else {
			return game.getNextPacManDir(target, true, Game.DM.PATH);
		}

		//for(int i=0;i<activePills.length;i++)
		//	targetsArray[i] = activePills[i];
		
		//for(int i = 0; i < activePowerPills.length; i++)
		//	targetsArray[activePills.length+i] = activePowerPills[i];		
		
		//return the next direction once the closest target has been identified
		//return game.getNextPacManDir(game.getTarget(current, targetsArray, true, G.DM.PATH), true, Game.DM.PATH);
	}
}