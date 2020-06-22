import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;


public class GraphVisualizer {
    public static int nodeCount = 0;
    public static int edgeCount = 0;
    private static boolean failed = false;
    private static long failedThreadID;

    private static List<Line> lineList = new ArrayList<>();
    private static int total = 0;
    private static StackPane container;
    private static LineChart<Number, Number> lineChart;

    public static void init(StackPane c) {
        container = c;

        NumberAxis xAxis = new NumberAxis(0, 1000, 100);
        NumberAxis yAxis = new NumberAxis(0, 1000, 100);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);

        container.getChildren().add(lineChart);
    }

    public static void addLine(double x1, double x2, double y1, double y2) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(x1, y1));
        series.getData().add(new XYChart.Data<>(x2, y2));

        Platform.runLater(() -> {
            if (total > 25) {
                init(container);
                total = 0;
            }
            lineChart.getData().add(series);
            total++;
        });
    }


    public static void addDot(double x, double y) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(x, y));
        Platform.runLater(() -> {
            lineChart.getData().add(series);
        });
    }

    public static void setLineList(List<Line> list) {
        lineList = list;
    }

    public static List<Line> getLineList() {
        return lineList;
    }

    public static void showFailedAttemptErrorMessage(long threadID){
        failed = true;
        failedThreadID=threadID;
    }

    public static long getFailedThreadID(){ return failedThreadID;}


    public static boolean isAttemptFailed(){
        return failed;
    }

    public static void resetFailedStatus(){
        failed = false;
    }
}
