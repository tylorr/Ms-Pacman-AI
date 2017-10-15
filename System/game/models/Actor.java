package game.models;

import game.system._Node;

public interface Actor extends Cloneable
{
    public Node getLocation();
    public int getDirection();
}
