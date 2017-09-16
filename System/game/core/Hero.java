package game.core;

public class Hero implements Cloneable
{
    private Node location;
    private int direction;

    protected Hero(Node _location, int _direction)
    {
        location = _location;
        direction = _direction;
    }

    protected Hero clone()
    {
        try
        {
            return (Hero)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

    public Node getLocation()
    {
        return location;
    }

    public int getDirection()
    {
        return direction;
    }

    protected void setDirection(int newDirection)
    {
        direction = newDirection;
    }

    protected void setLocation(Node newLocation)
   {
       location = newLocation;
   }

    public int[] getPossibleDirs(boolean includeReverse)
    {
        int numNeighbors = location.getNumNeighbors();

        if (numNeighbors == 0)
            return new int[0];

        Node[] nodes = location.neighbors;
        int[] directions;

        if (includeReverse || (direction < 0 || direction > 3))
            directions = new int[numNeighbors];
        else
            directions = new int[numNeighbors - 1];

        int index = 0;

        for (int i = 0; i < nodes.length; i++)
            if (nodes[i] != null)
            {
                if (includeReverse || (direction < 0 || direction > 3))
                    directions[index++] = i;
                else if (i != getReverse(direction))
                    directions[index++] = i;
            }

        return directions;
    }

    //Returns the reverse of the direction supplied
    private int getReverse(int direction)
    {
        switch(direction)
        {
            case 0: return 2;
            case 1: return 3;
            case 2: return 0;
            case 3: return 1;
        }

        return 4;
    }

}
