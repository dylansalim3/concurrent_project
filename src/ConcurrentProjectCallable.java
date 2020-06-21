import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProjectCallable {

    /*
        @param m - Timeout
        @param numOfLines - Node size
        @param t - Threads
     */

    public static void main(String[] args){
        run(1,11,2);
    }

    static void run(int m, int n, int t) {
        int numOfLines = n/2;
        boolean isNodeNumberOdd = n%2==1;
        boolean isEmptyTimeout = m <= 0;

        Graph graph = new Graph();
        GraphWorkerCallable[] graphWorkers = new GraphWorkerCallable[numOfLines];
        ExecutorService executorService = Executors.newFixedThreadPool(t);
        List<Future> lineFutures = new ArrayList<>();
        for (int i = 0; i < numOfLines; i++) {
            GraphWorkerCallable graphWorker = new GraphWorkerCallable(graph);
            graphWorkers[i] = graphWorker;
            Future lineFuture = executorService.submit(graphWorker);
            lineFutures.add(lineFuture);
        }


        List<Line> lineList = new ArrayList<>();
        if(!isEmptyTimeout){
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
        }

        try{
            for(int i=0;i<lineFutures.size();i++){
                Future<Line> future = lineFutures.get(i);
                if(executorService.isTerminated() && !future.isDone()){
                    lineFutures.get(i).cancel(true);
                }else{
                    Line generatedLine = future.get();
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
        }catch(ExecutionException executionExp){
            System.out.println("Execution Exception occurs");
            executionExp.printStackTrace();
        }

        if(isNodeNumberOdd){
            graph.generateNonDuplicateNode();
        }
        List<Node> nodeList = graph.getNodeList();

        printLineDetails(lineList,nodeList);

        executorService.shutdown();
    }


    public static void printLineDetails(List<Line> lineList,List<Node> nodeList) {
        GraphVisualizer.nodeCount = nodeList.size();
        GraphVisualizer.edgeCount = lineList.size();

        for (Line line : lineList) {
            Node n1 = line.getN1();
            Node n2 = line.getN2();
            nodeList.remove(n1);
            nodeList.remove(n2);
            GraphVisualizer.addLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
        }

        for (Node node : nodeList) {
            GraphVisualizer.addDot(node.getX(), node.getY());
        }

        GraphVisualizer.setLineList(lineList);
    }
}

