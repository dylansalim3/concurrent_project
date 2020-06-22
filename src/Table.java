import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

// Generate a table on JavaFX application
public class Table {

    // This constructor will generate the columns and populate the data with provided line list
    public Table(TableView<TableItems> tableView, List<Line> lineList) {
        TableColumn<TableItems, String> lineNumber = new TableColumn<>("Line No");
        lineNumber.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));

        TableColumn<TableItems, String> node1x = new TableColumn<>("Node 1 (X)");
        node1x.setCellValueFactory(new PropertyValueFactory<>("node1x"));

        TableColumn<TableItems, String> node1y = new TableColumn<>("Node 1 (Y)");
        node1y.setCellValueFactory(new PropertyValueFactory<>("node1y"));

        TableColumn<TableItems, String> node2x = new TableColumn<>("Node 2 (X)");
        node2x.setCellValueFactory(new PropertyValueFactory<>("node2x"));

        TableColumn<TableItems, String> node2y = new TableColumn<>("Node 2 (Y)");
        node2y.setCellValueFactory(new PropertyValueFactory<>("node2y"));

        tableView.setItems(getAllLines(lineList));
        tableView.getColumns().addAll(lineNumber, node1x, node1y, node2x, node2y);
    }

    // Convert a normal list to observable list
    public ObservableList<TableItems> getAllLines(List<Line> lineList) {
        ObservableList<TableItems> tableItems = FXCollections.observableArrayList();
        int number = 1;

        for (Line line: lineList) {
            Node n1 = line.getN1();
            Node n2 = line.getN2();
            tableItems.add(new TableItems(number, n1.getX(), n1.getY(), n2.getX(), n2.getY()));
            number++;
        }

        return tableItems;
    }
}

