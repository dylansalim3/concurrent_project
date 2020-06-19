import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProjectCallable {
//    public static void main(String[] args) {
//        // set to false if you want to test without gui
//        boolean gui = true;
//        if (gui) {
//            App.main(args);
//        } else {
//            new Draw();
//            run(1, 10000, 2);
//        }
//    }

    public static void main(String[] args) {
        // set to false if you want to test without gui
//        boolean gui = true;
//        if (gui) {
//            App.main(args);
//        } else {
//            new Draw();
        run(1, 10000, 4);
//        }
    }

    /*
        @param m - Timeout
        @param numOfLines - Node size
        @param t - Threads
     */
    static void run(int m, int n, int t) {
        int numOfLines = n/2;
        boolean isNodeNumberOdd = n%2==1;
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
        System.out.println("Line size :" + lineList.size());
        System.out.println("Line list :" + lineList.toString());
        System.out.println("Node Size: "+nodeList.size());
        System.out.println("Node list : "+nodeList.toString());

        for (Line line : lineList) {
            Node n1 = line.getN1();
            Node n2 = line.getN2();
//            Draw.addNode(n1.getX(), n1.getY());
//            Draw.addNode(n2.getX(), n2.getY());
//            Draw.addEdge(n1.getX(), n1.getY(), n2.getX(), n2.getY());
            System.out.println(n1.getX()+ " "+ n1.getY());
            System.out.println(n2.getX()+" "+ n2.getY());
        }
    }
}

