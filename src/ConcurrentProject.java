import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentProject {
    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Please insert the program timeout period.(seconds)");
//        int m = scan.nextInt();
//        System.out.println("Please insert the number of random floating numbers to be generated");
//        int n = scan.nextInt();
//        System.out.println("Please insert the number of threads");
//        int t = scan.nextInt();


        int m = 1;
        int n = 10000;
        int t = 4;
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
    }
}
