package game.system;
import game.models.Actor;
import game.models.Node;
import java.util.List;
import java.util.ArrayList;

public class _Actor implements Actor
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

    protected List<Node> getPath(Node to, boolean canReverse) { return location.getPath(to, canReverse, direction); }

    protected int[] getPossibleDirs(boolean canReverse)
    {
        int numNeighbors = location.getNumNeighbors();

        if (numNeighbors == 0)
            return new int[0];

        List<Node> nodes = location.getNeighbors();
        int[] directions;

        if (canReverse || (direction < 0 || direction > 3))
            directions = new int[numNeighbors];
        else
            directions = new int[numNeighbors - 1];

        int index = 0;

        for (int i = 0; i < nodes.size(); i++)
        if (nodes.get(i) != null)
        {
            if (canReverse || (direction < 0 || direction > 3))
                directions[index++] = i;
            else if (i != Node.getReverse(direction))
                directions[index++] = i;
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
    protected Node getTarget(Node[] targets, boolean nearest, boolean canReverse)
    {
        Node target = null;

        double min=Integer.MAX_VALUE;
        double max=-Integer.MAX_VALUE;

        for(int i=0;i<targets.length;i++)
        {
            double dist = getPath(targets[i], canReverse).size();

            if(nearest && dist<min)
            {
                min = dist;
                target = targets[i];
            }

            if(!nearest && dist > max)
            {
                max = dist;
                target = targets[i];
            }
        }

        return target;
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
