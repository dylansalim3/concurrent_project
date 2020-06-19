import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProjectForkJoin {
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
        run(1, 10000, 2);
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

