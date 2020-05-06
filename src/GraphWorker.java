public class GraphWorker implements Runnable {
    private Graph graph;
    private Line line;
    private boolean fail;

    public GraphWorker(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void run() {
        int i = 0;
        Line l;

        do {
            l = graph.drawLine();
            i++;
        } while (l == null && i < 20);
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
