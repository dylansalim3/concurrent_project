public class GraphWorker implements Runnable {
    private Graph graph;
    private Line line;
    private boolean pass;

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
        } while (l == null && i<20);
        if(l!=null){
            line=l;
            pass = true;
        }else{
            pass = false;
        }
        synchronized (this){
            notifyAll();
        }
    }

    public synchronized Line getLine() throws InterruptedException {
        if (line == null&&!pass) {
            wait();
        }
        return line;
    }
}
