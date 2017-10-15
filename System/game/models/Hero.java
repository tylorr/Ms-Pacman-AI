package game.models;
import java.util.List;

public interface Hero extends Actor
{
    public List<Integer> getPossibleDirs(boolean canReverse);
    public int getNextDir(Node to, boolean approach);

    public List<Node> getPossibleLocations(boolean canReverse);
    public List<Node> getPath(Node to);
    public Node getTarget(List<Node> targets, boolean nearest);
}