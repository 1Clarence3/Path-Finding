import java.util.LinkedList;
import java.util.*;
public class CSimpleGraph
{
    static CGraphNode m_root = null;
    ArrayList<CGraphNode> nodes = new ArrayList<CGraphNode>();
    static boolean restart = false;

    public CGraphNode getRootNode () {
        return m_root;
    }

    public void createNode(double x, double y, String data)
    {
        nodes.add(new CGraphNode(x,y,data));
        nodes.get(nodes.size()-1).adjacent = new ArrayList<Expansions>();
    }

    public void createNeighbors(CGraphNode start)
    {
        if(!(searchNodeXY(start.x+1,start.y) == null))
        {
            if(searchNodeXY(start.x+1,start.y).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x+1,start.y),5));
            }
        }
        if(!(searchNodeXY(start.x-1,start.y) == null))
        {
            if(searchNodeXY(start.x-1,start.y).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x-1,start.y),5));
            }
        }
        if(!(searchNodeXY(start.x+0.5,start.y+1) == null)) 
        {
            if(searchNodeXY(start.x+0.5,start.y+1).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x+0.5,start.y+1),5));
            }
        }
        if(!(searchNodeXY(start.x+0.5,start.y-1) == null)) 
        {
            if(searchNodeXY(start.x+0.5,start.y-1).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x+0.5,start.y-1),5));
            }
        }
        if(!(searchNodeXY(start.x-0.5,start.y+1) == null)) 
        {
            if(searchNodeXY(start.x-0.5,start.y+1).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x-0.5,start.y+1),5));
            }
        }
        if(!(searchNodeXY(start.x-0.5,start.y-1) == null))
        {
            if(searchNodeXY(start.x-0.5,start.y-1).wall == false)
            {
                start.adjacent.add(new Expansions(searchNodeXY(start.x-0.5,start.y-1),5));
            }
        }
    }

    public double getPathCost(CGraphNode start, CGraphNode end)
    {
        return ((int)((Math.abs(end.x-start.x)/(Math.sqrt(3)/4))/2))*5
        + ((int)((Math.abs(end.y-start.y)/(Math.sqrt(3)/4))/2))*5;
    }

    public void instantiateCost()
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            if(nodes.get(i).data.equals(m_root.data))
            {
                nodes.get(i).pathCost = 0;
                nodes.get(i).straightPathCost = 0;
                continue;
            }
            if(nodes.get(i).wall == false)
            {
                nodes.get(i).pathCost = ((int)((Math.abs(m_root.x-nodes.get(i).x)/(Math.sqrt(3)/4))/2))*5
                + ((int)((Math.abs(m_root.y-nodes.get(i).y)/(Math.sqrt(3)/4))/2))*5;
                nodes.get(i).straightPathCost = distance(nodes.get(i).x,nodes.get(i).y,m_root.x,m_root.y);
            }
        }
    }

    public static void connectNodes (CGraphNode a, CGraphNode b) {
        a.edges.add(b);
        b.edges.add(a);
        a.adjacent.add(new Expansions(b,distance(a.x,a.y,b.x,b.y)));
        b.adjacent.add(new Expansions(a,distance(a.x,a.y,b.x,b.y)));
    }

    public double pathCostFinder(CGraphNode a, CGraphNode b)
    {
        for(Expansions e : a.adjacent)
        {
            if(e.target.data.equals(b.data))
            {
                return e.cost;
            }
        }
        return 0;
    }

    public CGraphNode searchNodeXY(double x, double y)
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            if(x < nodes.get(i).x + 0.5 && x > nodes.get(i).x - 0.5 &&
            y < nodes.get(i).y + 0.5 && y > nodes.get(i).y - 0.5)
            {
                return nodes.get(i);
            }
        }
        return null;
    }

    public static double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
    }
}
