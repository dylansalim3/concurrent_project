import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProject {
    public static void main(String[] args) {
        // set to false if you want to test without gui
        boolean gui = true;
        if (gui) {
            App.main(args);
        } else {
            new Draw();
            run(1, 10000, 2);
        }
    }

    /*
        @param m - Timeout
        @param n - Node size
        @param t - Threads
     */
    static void run(int m, int n, int t) {
        Graph graph = new Graph();
        GraphWorker[] graphWorkers = new GraphWorker[n];
        ExecutorService executorService = Executors.newFixedThreadPool(t);
        List<Future> lineFutures = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            GraphWorker graphWorker = new GraphWorker(graph);
            graphWorkers[i] = graphWorker;
            Future lineFuture = executorService.submit(graphWorker);
            lineFutures.add(lineFuture);
        }

        try {
            if (!executorService.awaitTermination(m, TimeUnit.MILLISECONDS)) {
                executorService.shutdown();
                if (!executorService.isTerminated()) {
                    executorService.shutdownNow();
                }
                System.out.println("Terminated");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        List<Line> lineList = new ArrayList<>();
        try{
            for(int i=0;i<lineFutures.size();i++){
                Future future = lineFutures.get(i);
                if(executorService.isTerminated() && !future.isDone()){
                    lineFutures.get(i).cancel(true);
                }else{
                    lineList.add(graphWorkers[i].getLine());
                }
            }
        }catch(InterruptedException e){
            System.out.println("Interrupted Exception occurs");
            e.printStackTrace();
        }

        printLineDetails(lineList);

        executorService.shutdown();
    }


    public static void printLineDetails(List<Line> lineList) {
        System.out.println("Line size :" + lineList.size());
        System.out.println("Line list :" + lineList.toString());

        for (Line line : lineList) {
            Node n1 = line.getN1();
            Node n2 = line.getN2();
            Draw.addNode(n1.getX(), n1.getY());
            Draw.addNode(n2.getX(), n2.getY());
            Draw.addEdge(n1.getX(), n1.getY(), n2.getX(), n2.getY());
        }
    }
}
