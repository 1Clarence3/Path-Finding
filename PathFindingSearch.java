import java.util.*;
import java.awt.Font;
public class PathFindingSearch
{
    public static boolean ucs(CGraphNode source, CGraphNode goal, CSimpleGraph graph, boolean draw, int layers, int work)
    {
        if(source == null)
        {
            return false;
        }
        List<CGraphNode> list = new ArrayList<CGraphNode>();
        //source.pathCost = 0;
        PriorityQueue<CGraphNode> queue = new PriorityQueue<CGraphNode>(graph.nodes.size(),new Comparator<CGraphNode>(){
                    public int compare(CGraphNode i, CGraphNode j){
                        if(i.pathCost > j.pathCost){
                            return 1;
                        }

                        else if (i.pathCost < j.pathCost){
                            return -1;
                        }

                        else{
                            return 0;
                        }
                    }
                }

            );
        if(work == 1)
        {
            queue.add(graph.nodes.get(0));
        }
        else
        {
            queue.add(source);
        }
        Set<CGraphNode> explored = new HashSet<CGraphNode>();
        boolean found = false;
        List<CGraphNode> path = new ArrayList<CGraphNode>();
        double x, y;
        boolean breakLoop = false;
        do{
            if(StdDraw.mousePressed())
            {
                StdDraw.pause(200);
                x = StdDraw.mouseX();
                y = StdDraw.mouseY();
                if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                    StdDraw.text(layers+layers*0.25,layers*(0.33),"Start");
                    StdDraw.show();
                    while(true)
                    {
                        if(StdDraw.mousePressed())
                        {
                            StdDraw.pause(200);
                            x = StdDraw.mouseX();
                            y = StdDraw.mouseY();
                            if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                            {
                                StdDraw.setPenColor(StdDraw.WHITE);
                                StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                                StdDraw.text(layers+layers*0.25,layers*(0.33),"Pause");
                                StdDraw.show();
                                break;
                            }
                            else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                            {
                                graph.restart = true;
                                breakLoop = true;
                                break;
                            }
                        }                        
                    }
                    if(breakLoop == true)
                    {
                        break;
                    }
                }
                else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                {
                    graph.restart = true;
                    break;
                }
            }
            path.clear();
            CGraphNode current = queue.poll();
            current.visited = true;
            explored.add(current);
            if(current.wall == false)
            {
                if(draw == true && !current.data.equals(source.data) && !current.data.equals(goal.data))
                {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    Runner.filledHexagon(current.x,current.y,1/Math.sqrt(3));
                    StdDraw.setPenColor(StdDraw.BLACK);
                    Runner.hexagon(current.x,current.y,1/Math.sqrt(3));
                    StdDraw.show(10);
                }

                if (current.data.equals(goal.data)) {
                    goal.parent = current.parent;
                    goal.pathCost = current.pathCost;
                    found = true;
                    break;
                }

                for(Expansions e : current.adjacent) {
                    CGraphNode child = e.target;
                    if(child.visited == false)
                    {
                        e.target.visited = true;
                        double cost = e.cost;
                        if ((queue.contains(child) || explored.contains(child))
                        && !path.contains(child)) {
                            CGraphNode n = new CGraphNode(child);
                            list.add(n);
                            list.get(list.size() - 1).pathCost = current.pathCost + cost;
                            list.get(list.size() - 1).parent = current;
                            queue.add(list.get(list.size() - 1));
                            //System.out.println(list.get(list.size() - 1));
                            //System.out.println(queue);
                        } 
                        else if (!path.contains(child)) {
                            child.pathCost = current.pathCost + cost;
                            child.parent = current;
                            queue.add(child);
                            //System.out.println(child);
                            //System.out.println(queue);
                        }
                    }
                }
            }
        }while(!queue.isEmpty());
        if(draw == false)
        {
            List<CGraphNode> path1 = printPath(goal);
            for(int i = 0; i < path1.size(); i++)
            {
                StdDraw.setPenColor(StdDraw.CYAN);
                Runner.filledHexagon(Runner.searchX(path1.get(i).data,Runner.xy),Runner.searchY(path1.get(i).data,Runner.xy),1/Math.sqrt(3));
                StdDraw.setPenColor(StdDraw.BLACK);
                Runner.hexagon(Runner.searchX(path1.get(i).data,Runner.xy),Runner.searchY(path1.get(i).data,Runner.xy),1/Math.sqrt(3));
                StdDraw.show(200);
            }
        }
        return found;
    }

    public static boolean gs(CGraphNode source, CGraphNode goal, CSimpleGraph graph, int layers)
    {
        if(source == null)
        {
            return false;
        }
        double[] dist = new double[graph.nodes.size()];
        for(int i = 0; i < dist.length; i++)
        {
            dist[i] = Integer.MAX_VALUE;
        }
        ArrayList<CGraphNode> queue = new ArrayList<CGraphNode>();
        queue.add(source);
        double minDist = Integer.MAX_VALUE;
        int index = 0;
        int distIndex = 0;
        boolean found = false;
        ArrayList<CGraphNode> path = new ArrayList<CGraphNode>();
        PriorityQueue<CGraphNode> queue1 = new PriorityQueue<CGraphNode>(graph.nodes.size(),new Comparator<CGraphNode>(){
                    public int compare(CGraphNode i, CGraphNode j){
                        if(i.pathCost > j.pathCost){
                            return 1;
                        }

                        else if (i.pathCost < j.pathCost){
                            return -1;
                        }

                        else{
                            return 0;
                        }
                    }
                }
            );
        queue1.add(source);
        double x, y;
        boolean breakLoop = false;
        while(!queue.isEmpty())
        {
            if(StdDraw.mousePressed())
            {
                StdDraw.pause(200);
                x = StdDraw.mouseX();
                y = StdDraw.mouseY();
                if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                    StdDraw.text(layers+layers*0.25,layers*(0.33),"Start");
                    StdDraw.show();
                    while(true)
                    {
                        if(StdDraw.mousePressed())
                        {
                            StdDraw.pause(200);
                            x = StdDraw.mouseX();
                            y = StdDraw.mouseY();
                            if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                            {
                                StdDraw.setPenColor(StdDraw.WHITE);
                                StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                                StdDraw.text(layers+layers*0.25,layers*(0.33),"Pause");
                                StdDraw.show();
                                break;
                            }
                            else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                            {
                                graph.restart = true;
                                breakLoop = true;
                                break;
                            }
                        }
                    }
                    if(breakLoop == true)
                    {
                        break;
                    }
                }
                else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                {
                    graph.restart = true;
                    break;
                }
            }
            for(int i = 0; i < queue.size(); i++)
            {
                if(queue.get(i).straightPathCost < minDist)
                {
                    minDist = queue.get(i).straightPathCost;
                    index = i;
                }
            }
            CGraphNode curr = queue.remove(index); 
            if(curr.data.equals(goal.data))
            {
                found = true;
                break;
            }

            if(curr.wall == false)
            {
                if(!curr.data.equals(source.data) && !curr.data.equals(goal.data))
                {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    Runner.filledHexagon(curr.x,curr.y,1/Math.sqrt(3));
                    StdDraw.setPenColor(StdDraw.BLACK);
                    Runner.hexagon(curr.x,curr.y,1/Math.sqrt(3));
                    StdDraw.show(50);
                    path.add(curr);
                }

                for(Expansions e : curr.adjacent) {
                    if(!e.target.visited)
                    {
                        e.target.visited = true;
                        dist[distIndex] = e.cost;
                        queue.add(e.target);
                    }
                }
                distIndex++;
            }
        }

        CSimpleGraph graph1 = new CSimpleGraph();
        path.add(0,new CGraphNode(source.x,source.y,source.data));
        path.get(0).straightPathCost = source.straightPathCost;
        path.add(new CGraphNode(goal.x,goal.y,goal.data));
        path.get(path.size()-1).straightPathCost = goal.straightPathCost;
        for(int i = 0; i < path.size(); i++)
        {
            graph1.createNode(path.get(i).x,path.get(i).y,path.get(i).data);
            graph1.nodes.get(graph1.nodes.size()-1).straightPathCost = path.get(i).straightPathCost;
        }       
        graph1.m_root = graph1.nodes.get(graph1.nodes.size()-1);
        graph1.instantiateCost();
        for(int i = 0; i < path.size(); i++)
        {            
            if(!(graph.searchNodeXY(path.get(i).x,path.get(i).y) == null))
            {
                for(Expansions e : graph.searchNodeXY(path.get(i).x,path.get(i).y).adjacent)
                {
                    if(path.contains(e.target))
                    {
                        graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(e.target,e.cost));
                    }
                }
            }
            if(i == 1)
            {
                graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(graph.searchNodeXY(source.x,source.y),5));                
            }
            if(i == path.size()-2)
            {
                graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(graph.searchNodeXY(goal.x,goal.y),5));
            }
        }
        for(int i = 0; i < graph1.nodes.size(); i++)
        {
            graph1.nodes.get(i).visited = false;
            for(Expansions e : graph1.nodes.get(i).adjacent)
            {
                e.target.visited = false;
            }
        }
        ucs(path.get(0),path.get(path.size()-1),graph1,false,layers,1);

        /*
        for(int i = 0; i < path.size(); i++)
        {
        StdDraw.setPenColor(StdDraw.CYAN);
        Runner.filledHexagon(Runner.searchX(path.get(i).data,Runner.xy),Runner.searchY(path.get(i).data,Runner.xy),1/Math.sqrt(3));
        StdDraw.setPenColor(StdDraw.BLACK);
        Runner.hexagon(Runner.searchX(path.get(i).data,Runner.xy),Runner.searchY(path.get(i).data,Runner.xy),1/Math.sqrt(3));
        StdDraw.show(200);
        }

         */
        /*
        List<CGraphNode> path1 = printPath(graph1.nodes.get(graph1.nodes.size()-1));
        for(int i = 0; i < path1.size(); i++)
        {
        StdDraw.setPenColor(StdDraw.CYAN);
        Runner.filledHexagon(Runner.searchX(path.get(i).data,Runner.xy),Runner.searchY(path.get(i).data,Runner.xy),1/Math.sqrt(3));
        StdDraw.setPenColor(StdDraw.BLACK);
        Runner.hexagon(Runner.searchX(path.get(i).data,Runner.xy),Runner.searchY(path.get(i).data,Runner.xy),1/Math.sqrt(3));
        StdDraw.show(200);
        }
         */
        return found;
    }

    public static boolean as(CGraphNode source, CGraphNode goal, CSimpleGraph graph, int layers)
    {
        if(source == null)
        {
            return false;
        }

        ArrayList<CGraphNode> openSet = new ArrayList<CGraphNode>();

        PriorityQueue<CGraphNode> queue = new PriorityQueue<CGraphNode>(graph.nodes.size(),new Comparator<CGraphNode>(){
                    public int compare(CGraphNode i, CGraphNode j){
                        if(i.fCost > j.fCost){
                            return 1;
                        }

                        else if (i.fCost < j.fCost){
                            return -1;
                        }

                        else{
                            return 0;
                        }
                    }
                }

            );        
        Comparator<CGraphNode> comparator = new Comparator<CGraphNode>(){
                public int compare(CGraphNode a, CGraphNode b) {
                    if(a.fCost > b.fCost)
                    {
                        return 1;
                    }
                    else if (a.fCost < b.fCost){
                        return -1;
                    }

                    else{
                        return 0;
                    }
                }
            };

        source.gCost = 0;
        queue.add(source);
        openSet.add(source);
        source.fCost = source.gCost + source.straightPathCost;
        ArrayList<CGraphNode> explored = new ArrayList<CGraphNode>();
        boolean found = false;
        boolean breakLoop = false;
        double x, y;
        ArrayList<CGraphNode> path = new ArrayList<CGraphNode>();
        while((!openSet.isEmpty()))
        {
            if(StdDraw.mousePressed())
            {
                StdDraw.pause(200);
                x = StdDraw.mouseX();
                y = StdDraw.mouseY();
                if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                    StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                    StdDraw.text(layers+layers*0.25,layers*(0.33),"Start");
                    StdDraw.show();
                    while(true)
                    {
                        if(StdDraw.mousePressed())
                        {
                            StdDraw.pause(200);
                            x = StdDraw.mouseX();
                            y = StdDraw.mouseY();
                            if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.33)+layers*0.05 && y > layers*(0.33)-layers*0.05)
                            {
                                StdDraw.setPenColor(StdDraw.WHITE);
                                StdDraw.filledRectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setPenColor(StdDraw.BLACK);
                                StdDraw.rectangle(layers+layers*0.25,layers*(0.33),layers*0.17,layers*0.05);
                                StdDraw.setFont(new Font("Arial",Font.BOLD,18));
                                StdDraw.text(layers+layers*0.25,layers*(0.33),"Pause");
                                StdDraw.show();
                                break;
                            }
                            else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                            {
                                graph.restart = true;
                                breakLoop = true;
                                break;
                            }
                        }
                    }
                    if(breakLoop == true)
                    {
                        break;
                    }
                }
                else if(x < layers+layers*0.25+layers*0.17 && x > layers+layers*0.25-layers*0.17 && y < layers*(0.22)+layers*0.05 && y > layers*(0.22)-layers*0.05)
                {
                    graph.restart = true;
                    break;
                }
            }
            for(int i = 0; i < openSet.size(); i++)
            {
                openSet.get(i).gCost = ((int)((Math.abs(source.x-openSet.get(i).x)/(Math.sqrt(3)/4))/2))*5
                + ((int)((Math.abs(source.y-openSet.get(i).y)/(Math.sqrt(3)/4))/2))*5;
                openSet.get(i).fCost = openSet.get(i).gCost + ((int)((Math.abs(openSet.get(i).x-source.x)/(Math.sqrt(3)/4))/2))*5
                + ((int)((Math.abs(openSet.get(i).y-source.y)/(Math.sqrt(3)/4))/2))*5;
            }

            int winner = 0;
            openSet.sort(comparator);

            for(int i = 0; i < openSet.size(); i++)
            {
                if(openSet.get(i).fCost < openSet.get(winner).fCost)
                {
                    winner = i;
                }
            }         

            CGraphNode current = openSet.get(winner);
            openSet.remove(winner);
            if(current.data.equals(goal.data))
            {
                found = true;
                break;
            }
            /*
            for(int i = openSet.size()-1; i >= 0; i--)
            {
            if(openSet.get(i).data.equals(current.data))
            {
            //splice?
            openSet.remove(i);
            }
            }
             */
            explored.add(current);
            current.visited = true;

            if(current.wall == false)
            {
                if(!current.data.equals(source.data) && !current.data.equals(goal.data))
                {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    Runner.filledHexagon(current.x,current.y,1/Math.sqrt(3));
                    StdDraw.setPenColor(StdDraw.BLACK);
                    Runner.hexagon(current.x,current.y,1/Math.sqrt(3));
                    StdDraw.show(10);
                    path.add(current);
                }
                for(Expansions e : current.adjacent){
                    CGraphNode child = e.target;
                    if(!explored.contains(child) && !child.wall && child.visited == false)
                    {
                        //Will be refined
                        double tempgCost = current.pathCost + 5;
                        if(openSet.contains(child))
                        {
                            if(tempgCost < child.pathCost + child.straightPathCost)
                            {
                                child.fCost = current.gCost;
                                child.parent = current;
                                child.gCost = tempgCost;
                                child.fCost = child.gCost + child.straightPathCost;
                                child.straightPathCost = current.straightPathCost;
                                openSet.add(child);
                            }
                        }
                        else
                        {                            
                            child.pathCost = current.pathCost + 5;
                        }                        
                        e.target.visited = true;
                    }   
                }
            }
        }
        CSimpleGraph graph1 = new CSimpleGraph();
        path.add(0,new CGraphNode(source.x,source.y,source.data));
        path.get(0).straightPathCost = source.straightPathCost;
        path.add(new CGraphNode(goal.x,goal.y,goal.data));
        path.get(path.size()-1).straightPathCost = goal.straightPathCost;
        for(int i = 0; i < path.size(); i++)
        {
            graph1.createNode(path.get(i).x,path.get(i).y,path.get(i).data);
            graph1.nodes.get(graph1.nodes.size()-1).straightPathCost = path.get(i).straightPathCost;
        }       
        graph1.m_root = graph1.nodes.get(graph1.nodes.size()-1);
        graph1.instantiateCost();
        for(int i = 0; i < path.size(); i++)
        {            
            if(!(graph.searchNodeXY(path.get(i).x,path.get(i).y) == null))
            {
                for(Expansions e : graph.searchNodeXY(path.get(i).x,path.get(i).y).adjacent)
                {
                    if(path.contains(e.target))
                    {
                        graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(e.target,e.cost));
                    }
                }
            }
            if(i == 1)
            {
                graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(graph.searchNodeXY(source.x,source.y),5));                
            }
            if(i == path.size()-2)
            {
                graph1.searchNodeXY(path.get(i).x,path.get(i).y).adjacent.add(new Expansions(graph.searchNodeXY(goal.x,goal.y),5));
            }
        }
        for(int i = 0; i < graph1.nodes.size(); i++)
        {
            graph1.nodes.get(i).visited = false;
            for(Expansions e : graph1.nodes.get(i).adjacent)
            {
                e.target.visited = false;
            }
        }
        ucs(path.get(0),path.get(path.size()-1),graph1,false,layers,1);
        return found;
    }

    public static List<CGraphNode> printPath(CGraphNode target)
    {
        List<CGraphNode> path = new ArrayList<CGraphNode>();
        /*
        for(CGraphNode node = target; node != null; node = node.parent){
        path.add(node);
        }
        Collections.reverse(path);
        return path;
         */
        CGraphNode temp = target;
        path.add(temp);
        while(!(temp.parent == null))
        {
            path.add(temp.parent);
            temp = temp.parent;
        }
        Collections.reverse(path);
        return path;
    }

}
