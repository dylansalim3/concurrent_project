import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

// Table items object to populate the table's row
public class TableItems extends RecursiveTreeObject<TableItems> {
    public int lineNumber;
    public double node1x;
    public double node1y;
    public double node2x;
    public double node2y;

    public TableItems(int lineNo, double x1, double y1, double x2, double y2) {
        this.lineNumber = lineNo;
        this.node1x = x1;
        this.node1y = y1;
        this.node2x = x2;
        this.node2y = y2;
    }

    // Getter and setter

    public double getNode2y() {
        return node2y;
    }

    public double getNode2x() {
        return node2x;
    }

    public double getNode1y() {
        return node1y;
    }

    public double getNode1x() {
        return node1x;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setNode1x(double node1x) {
        this.node1x = node1x;
    }

    public void setNode1y(double node1y) {
        this.node1y = node1y;
    }

    public void setNode2x(double node2x) {
        this.node2x = node2x;
    }

    public void setNode2y(double node2y) {
        this.node2y = node2y;
    }
}
