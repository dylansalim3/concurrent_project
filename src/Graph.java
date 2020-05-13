import java.util.*;

public class Graph {
    private final double MAX_AXIS_SIZE = 10000.00;
    private List<Node> nodeList;

    public Graph() {
        nodeList = new ArrayList<>();
    }

    public Line drawLine(){
        Node n1 = generateNode();
        System.out.println("Node n1 created : "+n1.toString());
        Node n2 = generateNode();
//        Node n2 = new Node(n1.getX(),n1.getY());
        System.out.println("Node n2 created : "+n2.toString());
        synchronized (this){
            if(n1.equals(n2)||nodeList.contains(n1)||nodeList.contains(n2)){
                System.out.println("Line creation failed. Duplicate node detected.");
                return null;
            }else{
                nodeList.add(n1);
                nodeList.add(n2);
                Line line = new Line(n1,n2);
                System.out.println("Line creation successful. Line : "+line.toString());
                return line;
            }
        }
    }


    private Node generateNode(){
        double x = (double)Math.round(new Random().nextDouble()*MAX_AXIS_SIZE*100)/100;
        double y = (double)Math.round(new Random().nextDouble()*MAX_AXIS_SIZE*100)/100;
        Node node = new Node(x,y);
        return node;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }
}
