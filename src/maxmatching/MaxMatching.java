package maxmatching;

import com.sun.javafx.charts.ChartLayoutAnimator;
import java.awt.Button;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MaxMatching extends Application {

    private Circle source, target;
    private int counterNode = 0;
    private int counterEdge = 0;
    private static ArrayList<Node> nodes = new ArrayList<>();
    private static ArrayList<Edge> edges = new ArrayList<>();
    private static ArrayList<Edge> holdEdges = new ArrayList<>();
    private static ArrayList<Line> lines = new ArrayList<>();
    private boolean isSolved = false;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        javafx.scene.control.Button btn0 = new javafx.scene.control.Button();
        javafx.scene.control.Button btn1 = new javafx.scene.control.Button();
        btn0.setText("group 0");
        btn0.setLayoutX(50);
        btn0.setLayoutY(50);
        root.getChildren().add(btn0);
        btn1.setText("group 1");
        btn1.setLayoutX(300);
        btn1.setLayoutY(50);
        root.getChildren().add(btn1);

        javafx.scene.control.Button btnFinish = new javafx.scene.control.Button();
        btnFinish.setText("solve");
        btnFinish.setLayoutX(450);
        btnFinish.setLayoutY(550);
        root.getChildren().add(btnFinish);
        root.setOnMouseClicked(event -> {
            btnFinish.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if (!isSolved) {
                        isSolved = true;
                        MaxMatching maxMatching = new MaxMatching();
                        maxMatching.solve();
                    } else {
                        System.out.println("<<<<<You should run the program once again!!!>>>>>");
                    }
                }
            });
            if (event.isConsumed() || event.getSource().getClass().getSimpleName().equals("Circle")) {
                return;

            }
            Circle circle = new Circle(event.getSceneX(), event.getSceneY(), 15, Color.BLUEVIOLET);
            circle.setId(counterNode + "");
            btn0.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Node s = new Node(counterNode, false);
                    nodes.add(s);
                    circle.setFill(Color.DARKSALMON);
                    System.out.println("Node from " + s.number);
                    counterNode++;
                }
            });
            btn1.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Node s = new Node(counterNode, true);
                    nodes.add(s);
                    circle.setFill(Color.GREEN);
                    System.out.println("Node from " + s.number);
                    counterNode++;
                }
            });

            circle.setOnMouseDragged(eventDrag -> {
                source = (Circle) eventDrag.getSource();
                source.setCenterX(eventDrag.getSceneX());
                source.setCenterY(eventDrag.getSceneY());
                eventDrag.consume();
            });
            circle.setOnMouseClicked(eventClick -> {
                if (eventClick.isConsumed()) {
                    return;
                }
                if (source == null) {
                    source = (Circle) eventClick.getSource();
                    source.setFill(Color.SLATEGRAY);
                } else if (target == null) {
                    target = (Circle) eventClick.getSource();
                    if (!source.equals(target)) {
                        if (nodes.get(Integer.parseInt(source.getId())).group) {
                            source.setFill(Color.GREEN);
                        } else {
                            source.setFill(Color.DARKSALMON);
                        }

                        Line line = new Line(source.getCenterX(), source.getCenterY(), target.getCenterX(), target.getCenterY());
                        Edge e;
                        if (!nodes.get(Integer.parseInt(source.getId())).group) {
                            e = new Edge(counterEdge, nodes.get(Integer.parseInt(source.getId())), nodes.get(Integer.parseInt(target.getId())), false);
                        } else {
                            e = new Edge(counterEdge, nodes.get(Integer.parseInt(target.getId())), nodes.get(Integer.parseInt(source.getId())), false);
                        }
                        root.getChildren().add(line);
                        line.setId(String.valueOf(counterEdge));
                        edges.add(e);
                        lines.add(line);
                        counterEdge++;
                        line.toBack();
                        line.startXProperty().bind(source.centerXProperty());
                        line.startYProperty().bind(source.centerYProperty());
                        line.endXProperty().bind(target.centerXProperty());
                        line.endYProperty().bind(target.centerYProperty());
                        DoubleBinding lenghtBinding = Bindings.createDoubleBinding(() -> {
                            double subX = line.startXProperty().get() - line.endXProperty().get();
                            double subY = line.startYProperty().get() - line.endYProperty().get();
                            double pureLength = Math.sqrt(subX * subX + subY * subY);
                            double length = (1 / pureLength) * Math.pow(10, 3);
                            if (length > 25) {
                                length = 25;
                            }
                            if (length < 2) {
                                length = 2;
                            }
                            return length;
                        }, line.startXProperty(), line.startYProperty(), line.endXProperty(), line.endYProperty());
                        line.strokeWidthProperty().bind(lenghtBinding);
                        System.out.println("Edge from " + source.getId() + " to " + target.getId() + " added");
                    }
                    source = null;
                    target = null;
                }
                eventClick.consume();
            });
            root.getChildren().add(circle);
        });
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("**************************ATTENTION***************************");
        System.out.println("Only one time you can press the solve button!");
        System.out.println("You should set the group number for each node when you make it");
        System.out.println("**************************************************************");
        launch(args);
    }

    private void solve() {
        for (Edge e : edges) {
            e.start.neighborhood.add(e.end);
        }
        addRandEdge();
        boolean tm[] = new boolean[1];
        for (Node n : nodes) {
            if (!n.group && !n.covered) {
                tm[0] = false;
                holdEdges.clear();
                findAugmenting(n, true, tm);
                System.out.println("********************************");
                System.out.println("Until now these edges are in matching...");
                for (Edge e : edges) {
                    if (e.matching) {
                        System.out.println("Matching " + e.number);
                    } else {
                        System.out.println("NOT Matching !!" + e.number);
                    }
                }
                System.out.println("********************************");
                System.out.println("");
            }
        }
        System.out.println("********************************");
        System.out.println("********************************");
        System.out.println("Final result...");
        for (Edge e : edges) {
            if (e.matching) {
                System.out.println("Matching " + e.number);
                lines.get(e.number).setStroke(Color.PURPLE);//or line.setStyle("-fx-stroke: red;");
                System.err.println("Drawing line...");
            } else {
                System.out.println("NOT Matching !!" + e.number);
            }
        }
        System.out.println("********************************");
        System.out.println("********************************");
        System.out.println("DONE !!");
    }

    private void addRandEdge() {
        Edge e = edges.get(0);
        edges.remove(e);
        Edge x = new Edge(e.number, e.end, e.start, true);
        x.end.neighborhood.remove(e.end);
        x.start.neighborhood.add(e.start);
        x.start.covered = true;
        x.end.covered = true;
        edges.add(x);
    }

    private Edge getEdge(Node strt, Node end) {
        for (Edge e : edges) {
            if (e.start == strt && e.end == end) {
                return e;
            }
        }
        return null;
    }

    private void findAugmenting(Node start, boolean lastMatching, boolean tm[]) {
        for (Node n : start.neighborhood) {
            makeCover();
            System.out.println("start " + start.number + " end: " + n.number);
            boolean canContinue = true;
            for (Edge ee : holdEdges) {
                if (ee.end.number == n.number) {
                    canContinue = false;
                }
            }
            if (canContinue) {
                holdEdges.add(new Edge(n.number, start, n, false));
            } else {
                return;
            }
            Edge e = getEdge(start, n);
            if (e.matching != lastMatching) {
                if (!e.end.covered && e.end.group) {
                    tm[0] = true;
                    edges.remove(e);
                    if (e.matching) {
                        e.matching = false;
                    } else {
                        e.matching = true;
                    }
                    Edge x = new Edge(e.number, e.end, e.start, e.matching);
                    System.out.println("new edge " + x.start.number + " " + x.end.number);
                    x.end.neighborhood.remove(e.end);
                    x.start.neighborhood.add(e.start);
                    x.start.covered = true;
                    x.end.covered = true;
                    edges.add(x);
                    return;
                }
                findAugmenting(n, !lastMatching, tm);
                if (tm[0]) {
                    System.out.println("aug");
                    edges.remove(e);
                    if (e.matching) {
                        e.matching = false;
                    } else {
                        e.matching = true;
                    }
                    Edge x = new Edge(e.number, e.end, e.start, e.matching);
                    System.out.println("new edge " + x.start.number + " " + x.end.number);
                    x.end.neighborhood.remove(e.end);
                    x.start.neighborhood.add(e.start);
                    x.start.covered = true;
                    x.end.covered = true;
                    edges.add(x);
                    return;
                }
            }
        }
    }

    private void makeCover() {
        for (Node n : nodes) {
            n.covered = false;
        }
        for (Node n : nodes) {
            for (Node nn : n.neighborhood) {
                System.out.println(n.number + "-" + nn.number);
                Edge e = getEdge(n, nn);
                if (e != null && e.matching) {
                    n.covered = true;
                    nn.covered = true;
                }
            }
        }
    }
}

class Node {

    int number;
    ArrayList<Node> neighborhood;
    boolean covered;
    boolean walked;
    boolean group;

    public Node(int number, boolean group) {
        this.neighborhood = new ArrayList<>();
        this.number = number;
        this.group = group;
        covered = false;
        walked = false;
    }
}

class Edge {

    int number;
    Node start;
    Node end;
    boolean matching;

    public Edge(int number, Node start, Node end, boolean matching) {
        this.number = number;
        this.start = start;
        this.end = end;
        this.matching = matching;
    }
}
