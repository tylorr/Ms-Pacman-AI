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

    //This method returns the direction to take given some options (usually corresponding to the
    //neighbours of the node in question), moving either towards or away (closer in {true, false})
    //using one of the three distance measures.
    protected int getDirFromOptions(Node[] options, Node to, boolean closer)
    {

        int dir=-1;

        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;

        for(int i = 0; i < options.length; i++)
        {
            if(options[i] != null)
            {
                double dist = 0;
                dist = options[i].getPathDistance(to);

                if(closer && dist < min)
                {
                    min = dist;
                    dir = i;
                }

                if(!closer && dist > max)
                {
                    max = dist;
                    dir = i;
                }
            }
        }

        return dir;
    }
}
