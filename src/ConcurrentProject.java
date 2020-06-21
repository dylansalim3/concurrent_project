import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProject {

    /*
        @param m - Timeout
        @param numOfLines - Node size
        @param t - Threads
     */
    static void run(int m, int n, int t) {
        int numOfLines = n/2;
        int numOfRemainingNode = n%2;
        Graph graph = new Graph();
        GraphWorker[] graphWorkers = new GraphWorker[numOfLines];
        ExecutorService executorService = Executors.newFixedThreadPool(t);
        List<Future> lineFutures = new ArrayList<>();
        for (int i = 0; i < numOfLines; i++) {
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
                System.out.println(future);
                System.out.println(executorService.isTerminated());
                System.out.println(future.isDone());
                if(executorService.isTerminated() && !future.isDone()){
                    System.out.println("istermindated");
                    lineFutures.get(i).cancel(true);
                }else{
                    System.out.println(graphWorkers[i].getLine());
                    Line generatedLine = graphWorkers[i].getLine();
                    System.out.println(generatedLine);
                    if(generatedLine!=null){

                        lineList.add(generatedLine);
                    }else{
                        break;
                    }
                }
            }
        }catch(InterruptedException e){
            System.out.println("Interrupted Exception occurs");
            e.printStackTrace();
        }
        graph.generateNonDuplicateNode();
        List<Node> nodeList = graph.getNodeList();
        printLineDetails(lineList);

        executorService.shutdown();
    }


    public static void printLineDetails(List<Line> lineList) {
        System.out.println("Line size :" + lineList.size());
        System.out.println("Line list :" + lineList.toString());

        for (Line line : lineList) {
            Node n1 = line.getN1();
            Node n2 = line.getN2();
            GraphVisualizer.addLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            System.out.println(n1.getX()+ " "+ n1.getY());
            System.out.println(n2.getX()+" "+ n2.getY());
        }

        GraphVisualizer.nodeCount = lineList.size() * 2;
        GraphVisualizer.edgeCount = lineList.size();
    }
}
