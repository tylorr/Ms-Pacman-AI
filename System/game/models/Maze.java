package game.models;
import java.util.List;

/*
	 * Stores the mazes, each of which is a connected graph. The differences between the mazes are the connectivity
	 * and the coordinates (used for drawing or to compute the Euclidean distance). There are 3 built-in distance
	 * functions in total: Euclidean, Manhattan and Dijkstra's shortest path distance. The latter is pre-computed and
	 * loaded, the others are computed on the fly whenever getNextDir(-) is called.
	 */

public interface Maze
{
    public String getName();                    // Returns name of maze

    public Node getInitialHeroPosition();       // Returns the starting position of the hero
    public Node getInitialEnemiesPosition();    // Returns the starting position of the enemies (i.e., first node AFTER leaving the lair)

    public int getNumberPills();                // Total number of pills in the maze
    public int getNumberPowerPills();           // Total number of power pills in the maze
    public int getNumberOfNodes();              // Total number of nodes in the graph (i.e., those with pills, power pills and those that are empty)

    public List<Node> getPillNodes();               // Returns the indices to all the nodes that have pills
    public List<Node> getPowerPillNodes();          // Returns all the nodes that have power pills
    public List<Node> getJunctionNodes();           // Returns the indices to all the nodes that are junctions
}
