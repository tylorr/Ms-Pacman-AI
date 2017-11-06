package game.models;

import java.util.List;

public interface Actor extends Cloneable
{
    Node getLocation();
    int getDirection();

    List<Node> getPathTo(Node to);
    int getNextDir(Node to, boolean approach);

    Node getTarget(List<Node> targets, boolean nearest);

    int getReverse();
}
