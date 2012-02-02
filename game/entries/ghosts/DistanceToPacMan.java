package game.entries.ghosts;
import game.core.*;


public class DistanceToPacMan{
	  public double[] getValue(Game game) {
	    double[] distance = new double[Game.NUM_GHOSTS];
	    int pacManLoc = game.getCurPacManLoc();
        int ghostLoc = 0;
        int i;
	    for (i = 0; i < Game.NUM_GHOSTS; i++){
	    	ghostLoc = game.getCurGhostLoc(i);
	        distance[i] = game.getEuclideanDistance(ghostLoc, pacManLoc);
	        if (distance[i] < 0)
	        	distance[i] = -distance[i];
	    }    
	    return distance;
	  }
}
