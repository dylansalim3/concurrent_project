import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class concurrent_main {
    public static void main(String[] args) throws InterruptedException {
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Please insert the program timeout period.(seconds)");
//        int m = scan.nextInt();
//        System.out.println("Please insert the number of random floating numbers to be generated");
//        int n = scan.nextInt();
//        System.out.println("Please insert the number of threads");
//        int t = scan.nextInt();

//        Node n1 = new Node(0.1,0.1);
//        Node n2 = new Node(0.1,0.1);
//        System.out.println(n1.equals(n2));

        int m = 1;
        int n = 100;
        int t = 4;

        Draw draw = new Draw();
        draw.display();

        Graph graph = new Graph();
        GraphWorker[] graphWorkers = new GraphWorker[n];
        ExecutorService executorService = Executors.newFixedThreadPool(t);
        executorService.awaitTermination(m, TimeUnit.MILLISECONDS);

        for(int i=0;i<n;i++){
            GraphWorker graphWorker = new GraphWorker(graph);
            graphWorkers[i] = graphWorker;
            executorService.execute(graphWorker);
        }

        try {
            if (!executorService.awaitTermination(m, TimeUnit.MILLISECONDS)) {
                System.out.println("Terminated");
                executorService.shutdown();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        List<Line> lineList = new ArrayList<>();
        for(int i=0;i<n;i++){
            if(executorService.isTerminated()){
                printLineDetails(lineList);
                break;
            }else{
                lineList.add(graphWorkers[i].getLine());
                if(i==n-1){
                    printLineDetails(lineList);
                }
            }
        }

        executorService.shutdown();
    }

    public static void printLineDetails(List<Line> lineList){
        System.out.println("line size "+lineList.size());
        System.out.println("Line list :"+lineList.toString());
    }

}
