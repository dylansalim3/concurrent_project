import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ConcurrentProjectForkJoin {

    /*
        @param m - Timeout
        @param numOfLines - Node size
        @param t - Threads
     */
    // Entry method for ForkJoin
    static void run(int m, int n, int t) {
        int numOfLines = n / 2;
        boolean isNodeNumberOdd = n % 2 == 1;
        boolean isEmptyTimeout = m <= 0;

        Graph graph = new Graph();
        ForkJoinPool forkJoinPool = new ForkJoinPool(t);
        List<RecursiveTask<Line>> recursiveTaskList = new ArrayList<>();

        for (int i = 0; i < numOfLines; i++) {
            RecursiveTask<Line> recursiveTask = new GraphWorkerRecursiveTask(graph);
            recursiveTaskList.add(recursiveTask);
            forkJoinPool.execute(recursiveTask);
        }

        if (!isEmptyTimeout) {
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

        }

        List<Line> lineList = new ArrayList<>();

        try {
            for (int i = 0; i < recursiveTaskList.size(); i++) {
                RecursiveTask<Line> recursiveTask = recursiveTaskList.get(i);
                boolean isTaskIncomplete = recursiveTask.getRawResult() == null;
                if (!forkJoinPool.isTerminated() || !isTaskIncomplete) {
                    Line generatedLine = recursiveTask.join();
                    lineList.add(generatedLine);
                } else {
                    break;
                }
            }
        } catch (CancellationException e) {
            System.out.println(e.toString());
        }


        if (isNodeNumberOdd) {
            graph.generateNonDuplicateNode();
        }


        List<Node> nodeList = graph.getNodeList();


        printLineDetails(lineList, nodeList);

    }


    public static void printLineDetails(List<Line> lineList, List<Node> nodeList) {
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

