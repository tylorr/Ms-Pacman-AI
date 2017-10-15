package game.models;
import java.util.List;

public interface Enemy extends Actor
{
    public int getEdibleTime();
    public int getLairTime();
    public boolean isEdible();
    public int[] getPossibleDirs();
    public int getNextDir(Node to, boolean approach);
    public List<Node> getPath(Node to);
    public List<Node> getPossibleLocations();
    public Node getTarget(Node[] targets, boolean nearest);
    public boolean requiresAction();
}
