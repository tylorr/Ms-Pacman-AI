package game.system;
import game.models.Attacker;
import game.models.Node;
import java.util.List;

public class _Attacker extends _Actor implements Attacker
{
    public List<Integer> getPossibleDirs(boolean canReverse) { return super.getPossibleDirs(canReverse); }
    public int getNextDir(Node to, boolean approach) { return location.getNextDir(to, approach); }
    public List<Node> getPathTo(Node to) { return getPathTo(to, true); }
    public List<Node> getPossibleLocations(boolean canReverse) { return super.getPossibleLocations(canReverse); }
    public Node getTarget(List<Node> targets, boolean nearest) { return getTarget(targets, nearest, true); }

    protected _Attacker(_Node location, int direction)
    {
        super(location, direction);
    }
    protected _Attacker clone() { return (_Attacker)super.clone(); }
}
