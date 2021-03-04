import java.util.ArrayList;
public class CGraphNode
{
    public String data;
    public ArrayList <CGraphNode> edges;
    public int depth;
    public boolean visited;
    public ArrayList <Expansions> adjacent;
    public double pathCost;
    public CGraphNode parent;
    public double straightPathCost;
    public double gCost;   
    public double fCost = 0;
    public double x;
    public double y;
    public boolean wall;
    
    public CGraphNode () {
        data = "EMPTY";
        edges = new ArrayList <CGraphNode> ();
        visited = false;
    }
    
    public CGraphNode (CGraphNode node)
    {
        straightPathCost = 0;
        adjacent = node.adjacent;
        data = node.data;
        pathCost = node.pathCost;
        parent = node.parent;
        straightPathCost = node.straightPathCost;
        x = node.x;
        y = node.y;
        visited = node.visited;
    }
      
    public CGraphNode (double x1, double y1, String name) {
        data = "EMPTY";
        visited = false;
        x = x1;
        y = y1;
        data = name;
    }    
} 
