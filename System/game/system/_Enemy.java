package game.system;
import game.models.Node;
import game.models.Enemy;
import java.util.List;

public class _Enemy extends _Actor implements Enemy
{
    public int getEdibleTime()
    {
        return edibleTime;
    }
    public int getLairTime()
    {
        return lairTime;
    }
    public boolean isEdible()
    {
        return edibleTime > 0;
    }

    public int[] getPossibleDirs() { return getPossibleDirs(false); }
    public int getNextDir(Node to, boolean approach) { return location.getNextDir(to, approach, false, direction); }
    public List<Node> getPath(Node to) { return getPath(to, false); }
    public List<Node> getPossibleLocations() { return getPossibleLocations(false); }
    public Node getTarget(Node[] targets, boolean nearest) { return getTarget(targets, nearest, false); }

    public boolean requiresAction()
    {
        return (location.isJunction() && edibleTime == 0 || edibleTime % _Game.ENEMY_SPEED_REDUCTION != 0);
    }

    protected int edibleTime, lairTime;

    protected _Enemy(Node location, int direction, int _lairTime)
    {
        super(location, direction);
        edibleTime = 0;
        lairTime = _lairTime;
    }
    protected _Enemy clone()
    {
        return (_Enemy)super.clone();
    }
}
