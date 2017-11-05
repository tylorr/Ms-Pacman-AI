package game.system;
import game.models.Actor;
import game.models.Node;
import java.util.List;
import java.util.ArrayList;

public abstract class _Actor implements Actor
{
    Node location;
    int direction;

    public Node getLocation()
    {
        return location;
    }
    public int getDirection()
    {
        return direction;
    }

    protected List<Node> getPathTo(Node to, boolean canReverse) { return location.getPathTo(to, canReverse, direction); }

    protected List<Integer> getPossibleDirs(boolean canReverse)
    {
        ArrayList<Integer> directions = new ArrayList<Integer>();
        int numNeighbors = location.getNumNeighbors();

        if (numNeighbors == 0)
            return directions;

        List<Node> nodes = location.getNeighbors();

        for (int i = 0; i < nodes.size(); i++)
        {
            if (nodes.get(i) != null)
            {
                if (canReverse || (direction < 0 || direction > 3))
                    directions.add(i);
                else if (i != Node.getReverse(direction))
                    directions.add(i);
            }
        }

        return directions;
    }

    protected List<Node> getPossibleLocations(boolean canReverse)
    {
        List<Node> newLocations = location.getNeighbors();
        if (!canReverse)
            newLocations.set(Node.getReverse(direction), null);

        return newLocations;
    }

    //Returns the target closest from this actor's position
    protected Node getTarget(List<Node> targets, boolean nearest, boolean canReverse)
    {
        Node result = null;

        double min=Integer.MAX_VALUE;
        double max=-Integer.MAX_VALUE;

        for (Node target : targets)
        {
            double dist = getPathTo(target, canReverse).size();

            if(nearest && dist<min)
            {
                min = dist;
                result = target;
            }

            if(!nearest && dist > max)
            {
                max = dist;
                result = target;
            }
        }

        return result;
    }


    protected _Actor(Node _location, int _direction)
    {
        location = _location;
        direction = _direction;
    }

    protected _Actor clone()
    {
        try
        {
            return (_Actor)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            return null;
        }
    }
}
