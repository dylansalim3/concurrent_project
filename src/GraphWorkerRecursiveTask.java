import java.util.concurrent.RecursiveTask;

public class GraphWorkerRecursiveTask extends RecursiveTask<Line> {
    private Graph graph;
    private Line line;

    public GraphWorkerRecursiveTask(Graph graph){
        this.graph = graph;
    }

    @Override
    protected Line compute() {
        int attempts = 0;
        do {
            line = graph.drawLine();
            attempts++;
        } while (line == null && attempts < 20);
        return line;
    }
}
