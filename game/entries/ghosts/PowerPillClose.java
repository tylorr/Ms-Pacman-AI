package game.entries.ghosts;

import game.core.Game;

class PowerPillClose extends Decision{
	public DecisionNode getBranch(Game game){
		int distance = 0;
		int i;
		for(i = 0; i < game.getNumActivePowerPills(); i++){
			distance = game.getCurPacManLoc() - game.getPowerPillIndices()[i];
		    if (distance < 0)
		    	distance = -distance;
		    if (distance < 7)
		    	return this.trueNode;
		}
			return this.falseNode;
	}
}
