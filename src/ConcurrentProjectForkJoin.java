import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProjectForkJoin {

    /*
        @param m - Timeout
        @param numOfLines - Node size
        @param t - Threads
     */
    static void run(int m, int n, int t) {
        int numOfLines = n/2;
        boolean isNodeNumberOdd = n%2==1;
        Graph graph = new Graph();
        ForkJoinPool forkJoinPool = new ForkJoinPool(t);
        List<RecursiveTask<Line>> recursiveTaskList = new ArrayList<>();

        for (int i = 0; i < numOfLines; i++) {
            RecursiveTask<Line> recursiveTask = new GraphWorkerRecursiveTask(graph);
            recursiveTaskList.add(recursiveTask);
            forkJoinPool.execute(recursiveTask);
        }

        try {
            if (!forkJoinPool.awaitTermination(m, TimeUnit.MILLISECONDS)) {
                forkJoinPool.shutdown();
                if (!forkJoinPool.isTerminated()) {
                    forkJoinPool.shutdownNow();
                }
                System.out.println("Terminated");
            }
        } catch (InterruptedException e) {
            forkJoinPool.shutdownNow();
        }

        List<Line> lineList = new ArrayList<>();
//        try{
            for(int i=0;i<recursiveTaskList.size();i++){
                RecursiveTask<Line> recursiveTask = recursiveTaskList.get(i);
                boolean isTaskIncomplete = recursiveTask.getRawResult()==null;
                if(!isTaskIncomplete){
                    Line generatedLine = recursiveTask.join();
                    lineList.add(generatedLine);
                }else{
                    break;
                }
            }

        if(isNodeNumberOdd){
            graph.generateNonDuplicateNode();
        }



        List<Node> nodeList = graph.getNodeList();


        printLineDetails(lineList,nodeList);

//        executorService.shutdown();
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

        if (nodeList.size() != 0) {
            Node n = nodeList.get(0);
            GraphVisualizer.addDot(n.getX(), n.getY());
        }

        GraphVisualizer.setLineList(lineList);
    }
}

