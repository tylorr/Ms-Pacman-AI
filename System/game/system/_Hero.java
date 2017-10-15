package game.system;
import game.models.Hero;
import game.models.Node;
import java.util.List;

public class _Hero extends _Actor implements Hero
{
    public int[] getPossibleDirs(boolean canReverse) { return super.getPossibleDirs(canReverse); }
    public int getNextDir(Node to, boolean approach) { return location.getNextDir(to, approach, true, direction); }
    public List<Node> getPath(Node to) { return getPath(to, false); }
    public List<Node> getPossibleLocations(boolean canReverse) { return super.getPossibleLocations(canReverse); }
    public Node getTarget(Node[] targets, boolean nearest) { return getTarget(targets, nearest, true); }

    protected _Hero(Node location, int direction)
    {
        super(location, direction);
    }
    protected _Hero clone() { return (_Hero)super.clone(); }
}
