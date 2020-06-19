public class GraphWorker implements Runnable {
    private Graph graph;
    private Line line;
    private boolean fail=false;

    public GraphWorker(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void run() {
        int attempts = 0;
        Line l;
        do {
            l = graph.drawLine();
            attempts++;
        } while (l == null && attempts < 20);
        if (l != null) {
            line = l;
        } else {
            fail = true;
        }
        synchronized (this) {
            notifyAll();
        }
    }

    public synchronized Line getLine() throws InterruptedException {
        if (line == null && !fail) {
            wait();
        }
        return line;
    }
}
