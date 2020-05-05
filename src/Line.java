public class Line {
    private Node n1;
    private Node n2;

    public Line(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    @Override
    public String toString() {
        return "Line{" +
                "n1=" + n1 +
                ", n2=" + n2 +
                '}';
    }
}
