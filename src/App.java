import java.util.function.UnaryOperator;

import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.Label;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
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
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Draw draw = new Draw();

        // Read fxml and set controller
        FXMLLoader loader = new FXMLLoader();
        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load(getClass().getResourceAsStream("/layout.fxml"));
        controller.formatText();

        // Draw graph
        FxViewer viewer = new FxViewer(Draw.getSgraph(), FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        FxDefaultView view = (FxDefaultView) viewer.addDefaultView(true);
        controller.graphParent.getChildren().add(view);
        view.prefHeightProperty().bind(controller.graphParent.heightProperty());
        view.prefWidthProperty().bind(controller.graphParent.widthProperty());
        view.toBack();

        // Show and maximize windows
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Concurrent Project");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

class Controller {
    @FXML
    StackPane graphParent;
    @FXML
    JFXTextField nodesizeInput;
    @FXML
    JFXTextField threadsInput;
    @FXML
    JFXTextField timeoutInput;
    @FXML
    Label nodeCount;
    @FXML
    Label lineCount;

    void formatText() {
        UnaryOperator<Change> filter = change -> (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null;
        nodesizeInput.setTextFormatter(new TextFormatter<>(filter));
        threadsInput.setTextFormatter(new TextFormatter<>(filter));
        timeoutInput.setTextFormatter(new TextFormatter<>(filter));
    }

    boolean validateInput() {
        String nodesize = nodesizeInput.getText();
        String threads = threadsInput.getText();
        String timeout = timeoutInput.getText();

        if (nodesize.length() == 0 || threads.length() == 0 || timeout.length() == 0) {
            displayError("Please fill in all fields before run");
            return false;
        }

        if (Integer.parseInt(threads) > Integer.parseInt(nodesize)) {
            displayError("Threads count should be smaller or equal to nodes size");
            return false;
        }

        return true;
    }

    void displayError(String message) {
        System.out.println(message);
    }

    @FXML
    void startClicked(Event e) {
        if (!validateInput()) {
            return;
        }

        int nodesize = Integer.parseInt(nodesizeInput.getText());
        int threads = Integer.parseInt(threadsInput.getText());
        int timeout = Integer.parseInt(timeoutInput.getText());

        Draw.clear();
        ConcurrentProject.run(timeout, nodesize, threads);
        setNodeCount(Draw.getNodeCount());
        setLineCount(Draw.getEdgeCount());
    }

    void setNodeCount(int count) {
        nodeCount.setText(String.valueOf(count));
    }

    void setLineCount(int count) {
        lineCount.setText(String.valueOf(count));
    }
}
