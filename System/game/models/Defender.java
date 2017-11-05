package game.models;
import java.util.List;

public interface Defender extends Actor
{
    int getVulnerableTime();
    int getLairTime();

    boolean isVulnerable();

    List<Integer> getPossibleDirs();
    int getNextDir(Node to, boolean approach);

    List<Node> getPossibleLocations();
    Node getTarget(List<Node> targets, boolean nearest);
    boolean requiresAction();
}
