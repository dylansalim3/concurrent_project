import java.util.concurrent.Callable;

public class GraphWorkerCallable implements Callable<Line> {
    private Graph graph;
    private Line line;
    private boolean fail=false;

    public GraphWorkerCallable(Graph graph){
        this.graph = graph;
    }

    // Methods that attempts to draw lines on the graph for 20 times
    @Override
    public Line call() throws Exception {
        int attempts = 0;
        do {
            line = graph.drawLine();
            attempts++;
        } while (line == null && attempts < 20);
        return line;
    }
}
