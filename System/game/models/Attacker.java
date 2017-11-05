package game.models;
import java.util.List;

public interface Attacker extends Actor
{
    List<Integer> getPossibleDirs(boolean canReverse);
    int getNextDir(Node to, boolean approach);

    List<Node> getPossibleLocations(boolean canReverse);
    Node getTarget(List<Node> targets, boolean nearest);
}