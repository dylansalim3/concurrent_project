import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class Draw {
    private static SingleGraph sgraph;

    public Draw() {
        sgraph = new SingleGraph("s_graph");
    }

    public static void addNode(double x, double y) {
        String id = generateId(x, y);

        if (sgraph.getNode(id) != null) {
            System.out.println("GUI ERROR: Duplicated node");
            return;
        }
        Node node = sgraph.addNode(id);
        node.setAttribute("ui.label", id);
        node.setAttribute("xy", x, y);
    }

    public static void addEdge(double x1, double y1, double x2, double y2) {
        String node1_id = generateId(x1, y1);
        String node2_id = generateId(x2, y2);
        String edge_id = node1_id + "|" + node2_id;

        if (sgraph.getNode(node1_id).edges().count() > 0 ||
            sgraph.getNode(node2_id).edges().count() > 0) {
            System.out.println("GUI ERROR: More than one edge");
            return;
        }
        sgraph.addEdge(edge_id, node1_id, node2_id);
    }

    /**
     * @link https://stackoverflow.com/questions/44675827/how-to-zoom-into-a-graphstream-view
     */
    public void display() {
        sgraph.display(false);
    }

    private static String generateId(double x, double y) {
        return "x:" + x + "," + "y:" + y;
    }
}
