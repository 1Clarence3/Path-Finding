
/**
 * Write a description of class Expansions here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Expansions
{
    public final double cost;
    public final CGraphNode target;

    public Expansions(CGraphNode targetNode, double costVal){
        cost = costVal;
        target = targetNode;
    }
}
