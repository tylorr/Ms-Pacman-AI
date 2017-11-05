package game.models;

import java.util.List;

public interface Actor extends Cloneable
{
    Node getLocation();
    int getDirection();
    List<Node> getPathTo(Node to);
}
