import java.util.ArrayList;

public class Main {

    public static String[] modes = {
            "Callable",
            "Callable (Custom Lock)",
            "Fork Join",
            "Fork Join (Custom Lock)"
    };

    /*
     * @param mode
     * @param m - Timeout
     * @param n - Node size
     * @param t - Threads
     * */
    public static void run(int mode, int m, int n, int t) {
        switch (mode) {
            case 1:
                ConcurrentProjectCallable.run(m, n, t);
                break;
            case 2:
                ConcurrentProjectCallableCustomLock.run(m, n, t);
                break;
            case 3:
                ConcurrentProjectForkJoin.run(m, n, t);
                break;
            case 4:
                ConcurrentProjectForkJoinCustomLock.run(m, n, t);
                break;
        }
    }
}
