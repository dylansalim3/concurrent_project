import java.util.function.UnaryOperator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.controlsfx.control.Notifications;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Read fxml and set controller
        FXMLLoader loader = new FXMLLoader();
        FXController controller = new FXController();
        loader.setController(controller);
        Parent root = loader.load(getClass().getResourceAsStream("/layout.fxml"));
        controller.formatText();

        // Create scene and load css
        Scene scene = new Scene(root);
        stage.setScene(scene);
        java.net.URL css = getClass().getResource("styles.css");
        scene.getStylesheets().add(css.toExternalForm());

        // Create a line chart
        GraphVisualizer.init(controller.chartContainer);

        // Show and maximize window
        stage.setTitle("Concurrent Project");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

class FXController {
    @FXML
    StackPane chartContainer;
    @FXML
    StackPane tableContainer;
    @FXML
    JFXTextField nodesizeInput;
    @FXML
    JFXTextField threadsInput;
    @FXML
    JFXTextField timeoutInput;
    @FXML
    JFXComboBox<Label> modeSelector;
    @FXML
    Label nodeCount;
    @FXML
    Label lineCount;
    @FXML
    JFXButton table;

    @FXML
    public void initialize() {
        for (String mode : Main.modes) {
            modeSelector.getItems().add(new Label(mode));
        }
        modeSelector.getSelectionModel().selectFirst();
    }

    void formatText() {
        UnaryOperator<Change> filter = change -> (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null;
        UnaryOperator<Change> filter2 = change -> (change.getControlNewText().matches("([0-9][0-9]*)?")) ? change : null;
        nodesizeInput.setTextFormatter(new TextFormatter<>(filter));
        threadsInput.setTextFormatter(new TextFormatter<>(filter));
        timeoutInput.setTextFormatter(new TextFormatter<>(filter2));
    }

    boolean validateInput() {
        String nodesize = nodesizeInput.getText();
        String threads = threadsInput.getText();
        String timeout = timeoutInput.getText();

        if (nodesize.length() == 0 || threads.length() == 0 || timeout.length() == 0) {
            displayMessage("Please fill in all fields before run", "warning");
            return false;
        }

        if (Integer.parseInt(threads) == 0) {
            displayMessage("Should have at least one thread", "warning");
            return false;
        }

        return true;
    }

    void displayMessage(String message, String mode) {
        Notifications notifications = Notifications.create()
                .title(mode)
                .text(message);

        switch (mode) {
            case "warning":
                notifications.showWarning();
                break;
            case "error":
                notifications.showError();
                break;
            case "info":
                notifications.showInformation();
                break;
        }
    }

    @FXML
    void startClicked(Event e) {
        if (!validateInput()) return;

        int nodeSize = Integer.parseInt(nodesizeInput.getText());
        int threads = Integer.parseInt(threadsInput.getText());
        int timeout = Integer.parseInt(timeoutInput.getText());
        int mode = modeSelector.getSelectionModel().getSelectedIndex();

        chartContainer.getChildren().clear();
        GraphVisualizer.init(chartContainer);
        GraphVisualizer.failed = false;

        displayMessage("Running", "info");

        Main.run(mode + 1, timeout, nodeSize, threads);
        setNodeCount(GraphVisualizer.nodeCount);
        setLineCount(GraphVisualizer.edgeCount);
        displayMessage("Rendering graph", "info");

        Platform.runLater(() -> {
            displayMessage("Done", "info");

            if (GraphVisualizer.failed) {
                displayMessage("One of the threads has failed to form a single edge after 20 attempts.", "error");
            }
        });
    }

    void setNodeCount(int count) {
        nodeCount.setText(String.valueOf(count));
    }

    void setLineCount(int count) {
        lineCount.setText(String.valueOf(count));
    }

    @FXML
    void tableClicked(Event e) {
        if (tableContainer.getChildren().size() > 0) {
            tableContainer.getChildren().clear();
        } else {
            TableView<TableItems> tableView = new TableView<>();
            tableContainer.getChildren().add(tableView);
            new Table(tableView, GraphVisualizer.getLineList());
        }

        table.setText(table.getText().equals("Graph") ? "Table" : "Graph");
    }
}
