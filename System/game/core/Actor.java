package game.core;

public class Actor implements Cloneable
{
    protected Node location;
    protected int direction;

    public Node getLocation()
    {
        return location;
    }

    public int getDirection()
    {
        return direction;
    }

    protected Actor(Node _location, int _direction)
    {
        location = _location;
        direction = _direction;
    }

    protected Actor clone()
    {
        try
        {
            return (Actor)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

    //Returns the reverse of the direction supplied
    protected int getReverse(int direction)
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
