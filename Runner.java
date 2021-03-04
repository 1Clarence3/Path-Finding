import javax.swing.*;  
import java.io.*;
import java.awt.Font;
import java.util.*;
public class Runner
{
    public static ArrayList<BackUp> xy;

    public static void main (String args []) {
        String playAgain = "Yes";
        String layers = "2";
        int loop = 1;
        Object[] searches = {"UCS - Uniform Cost Search","GS - Greedy Search","A* - A Star Search"};
        Object[] yesNo = {"Yes","No"};
        JFrame frame = new JFrame(); 
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLUE);        
        int layer = 0;
        CGraphNode start = new CGraphNode();
        CGraphNode end = new CGraphNode();
        xy = new ArrayList<BackUp>();
        while(playAgain.equals("Yes"))
        {
            CSimpleGraph graph = new CSimpleGraph();
            loop = 1;
            while(loop == 1 || Integer.parseInt(layers) < 2)
            {
                layers = JOptionPane.showInputDialog("How many layers would you like the hex grid to have? \nPlease input in a whole number greater than 1 and without spaces");
                try
                {
                    layer = Integer.parseInt(layers);
                    loop = 0;
                }
                catch(Exception e)
                {
                    loop = 1;
                }
            }
            double c = Math.sqrt(3) / 2;
            int n = layer;
            StdDraw.setCanvasSize(900, 600);
            StdDraw.setXscale(-1, 3.0*n/2);
            StdDraw.setYscale(-1, n);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    StdDraw.setPenRadius();
                    StdDraw.setPenColor(StdDraw.BLACK);
                    //filledHexagon(i+0.5*j, j*c, 0.5/c);
                    StdDraw.setPenRadius(0.005);
                    //StdDraw.setPenColor(StdDraw.WHITE);
                    if(j%2 == 1)
                    {
                        hexagon(i, j*c, 0.5/c); 
                        graph.createNode(i,j*c,String.valueOf(i) + ", " + String.valueOf(j)); 
                        xy.add(new BackUp(i,j*c,""+i+", "+j));
                    }
                    else
                    {
                        hexagon(i+(Math.sqrt(3)/2)*(0.5/c),j*c,0.5/c);  
                        graph.createNode(i+(Math.sqrt(3)/2)*(0.5/c),j*c,String.valueOf(i) + ", " + String.valueOf(j)); 
                        xy.add(new BackUp(i+(Math.sqrt(3)/2)*(0.5/c),j*c,""+i+", "+j));
                    }                
                }
            }        
            StdDraw.show();
            double x, y;
            //JOptionPane.showMessageDialog(null,"Click on a starting node");
            while(true)
            {
                if(StdDraw.mousePressed())
                {
                    x = StdDraw.mouseX();
                    y = StdDraw.mouseY();
                    if(graph.searchNodeXY(x,y) != null)
                    {
                        start = graph.searchNodeXY(x,y);
                        StdDraw.setPenColor(StdDraw.GREEN);
                        filledHexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                        StdDraw.setPenColor(StdDraw.BLACK);
                        hexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                        StdDraw.show();
                        StdDraw.pause(300);
                        break;
                    }                    
                }
            }

            while(true)
            {
                if(StdDraw.mousePressed())
                {
                    x = StdDraw.mouseX();
                    y = StdDraw.mouseY();                    
                    if(graph.searchNodeXY(x,y) != null)
                    {
                        end = graph.searchNodeXY(x,y);
                        graph.m_root = (graph.searchNodeXY(x,y));                                               
                        StdDraw.setPenColor(StdDraw.RED);
                        filledHexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                        StdDraw.setPenColor(StdDraw.BLACK);
                        hexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                        StdDraw.show(300);     
                        break;
                    }
                }
            }                    
            JOptionPane.showMessageDialog(null,"Press \"e\" to stop creating barriers \n Note: Please do not make walls that prevent a path from being found");
            while(true)
            {
                if(StdDraw.hasNextKeyTyped())
                {
                    if(StdDraw.isKeyPressed(69))
                    {
                        StdDraw.pause(150);
                        break;
                    }
                }
                if(StdDraw.mousePressed())
                {
                    x = StdDraw.mouseX();
                    y = StdDraw.mouseY();                    
                    if(graph.searchNodeXY(x,y) != null)
                    {
                        if(graph.searchNodeXY(x,y).wall == false)
                        {
                            graph.searchNodeXY(x,y).wall = true;
                            StdDraw.setPenColor(StdDraw.GRAY);
                            filledHexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                            StdDraw.setPenColor(StdDraw.BLACK);
                            hexagon(graph.searchNodeXY(x,y).x,graph.searchNodeXY(x,y).y,1/Math.sqrt(3));
                        }
                    }
                    StdDraw.show(50);
                }
            }
            graph.instantiateCost(); 
            for(int i = 0; i < graph.nodes.size(); i++)
            {
                graph.createNeighbors(graph.nodes.get(i));
            }
            StdDraw.rectangle(n+n*0.25,n*(0.66),n*0.17,n*0.05);
            StdDraw.rectangle(n+n*0.25,n*(0.55),n*0.17,n*0.05);
            StdDraw.rectangle(n+n*0.25,n*(0.44),n*0.17,n*0.05);
            StdDraw.rectangle(n+n*0.25,n*(0.33),n*0.17,n*0.05);
            StdDraw.rectangle(n+n*0.25,n*(0.22),n*0.17,n*0.05);
            StdDraw.setFont(new Font("Arial",Font.BOLD,18));
            StdDraw.text(n+n*0.25,n*(0.66),"Uniform Cost Search");
            StdDraw.text(n+n*0.25,n*(0.55),"Greedy Search");
            StdDraw.text(n+n*0.25,n*(0.44),"A Star Search");
            StdDraw.text(n+n*0.25,n*(0.33),"Pause");
            StdDraw.text(n+n*0.25,n*(0.22),"Restart");
            StdDraw.show();
            while(true)
            {
                if(graph.restart == true)
                {
                    graph.restart = false;
                    break;
                }
                if(StdDraw.mousePressed())
                {
                    StdDraw.pause(500);
                    x = StdDraw.mouseX();
                    y = StdDraw.mouseY();
                    if(x < n+n*0.25+n*0.17 && x > n+n*0.25-n*0.17 && y < n*(0.66)+n*0.05 && y > n*(0.66)-n*0.05)
                    {
                        if(PathFindingSearch.ucs(start,end,graph,true,n,0))
                        {                            
                            List<CGraphNode> path = PathFindingSearch.printPath(end);                            
                            for(int i = 1; i < path.size()-1; i++)
                            {
                                StdDraw.setPenColor(StdDraw.CYAN);
                                filledHexagon(searchX(path.get(i).data,xy),searchY(path.get(i).data,xy),1/Math.sqrt(3));
                                StdDraw.setPenColor(StdDraw.BLACK);
                                hexagon(searchX(path.get(i).data,xy),searchY(path.get(i).data,xy),1/Math.sqrt(3));
                                StdDraw.show(200);
                            }
                            StdDraw.setPenColor(StdDraw.GREEN);
                            StdDraw.setFont(new Font("Arial",Font.BOLD,50));
                            StdDraw.text(n/2,n/2,"PATH WAS FOUND!");
                            StdDraw.show();
                            break;
                        }
                    }
                    else if(x < n+n*0.25+n*0.17 && x > n+n*0.25-n*0.17 && y < n*(0.55)+n*0.05 && y > n*(0.55)-n*0.05)
                    {
                        if(PathFindingSearch.gs(start,end,graph,n))
                        {     
                            StdDraw.setPenColor(StdDraw.GREEN);
                            StdDraw.setFont(new Font("Arial",Font.BOLD,50));
                            StdDraw.text(n/2,n/2,"PATH WAS FOUND!");
                            StdDraw.show();
                            break;
                        }
                    }
                    else if(x < n+n*0.25+n*0.17 && x > n+n*0.25-n*0.17 && y < n*(0.44)+n*0.05 && y > n*(0.44)-n*0.05)
                    {
                        if(PathFindingSearch.gs(start,end,graph,n))
                        {                            
                            StdDraw.setPenColor(StdDraw.GREEN);
                            StdDraw.setFont(new Font("Arial",Font.BOLD,50));
                            StdDraw.text(n/2,n/2,"PATH WAS FOUND!");
                            StdDraw.show();
                            break;
                        }
                    }                    
                }
            }
            playAgain = (String)JOptionPane.showInputDialog(frame,"Would you like to play again?","UserInput",JOptionPane.PLAIN_MESSAGE,null,yesNo,"Yes");
            StdDraw.clear();
            StdDraw.show();
        }
    }

    public static void filledHexagon(double x, double y, double size) {
        double c1 = Math.sqrt(3) / 2;
        double c2 = 0.5;
        double[] px = { x + c1*size, x + c1*size, x, x - c1*size, x - c1*size, x};
        double[] py = { y - c2*size, y + c2*size, y + size, y + c2*size, y - c2*size, y - size };
        StdDraw.filledPolygon(px, py);
    }

    public static void hexagon(double x, double y, double size) {
        double c1 = Math.sqrt(3) / 2;
        double c2 = 0.5;
        double[] px = { x + c1*size, x + c1*size, x, x - c1*size, x - c1*size, x};
        double[] py = { y - c2*size, y + c2*size, y + size, y + c2*size, y - c2*size, y - size };
        StdDraw.polygon(px, py);
    }

    public static double searchX(String data, ArrayList<BackUp> xy)
    {
        for(int i = 0; i < xy.size(); i++)
        {
            if(data.equals(xy.get(i).val))
            {
                return xy.get(i).x;
            }
        }
        return 0.0;
    }

    public static double searchY(String data, ArrayList<BackUp> xy)
    {
        for(int i = 0; i < xy.size(); i++)
        {
            if(data.equals(xy.get(i).val))
            {
                return xy.get(i).y;
            }
        }
        return 0.0;
    }
}
