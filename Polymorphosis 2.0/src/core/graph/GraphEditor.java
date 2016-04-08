package core.graph;

import core.App;
import core.graph.map.Edge;
import core.graph.map.Node;
import core.graph.map.XmlMap;
import core.gui.Keyboard;
import toxi.geom.Circle;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

import java.util.ArrayList;


public class GraphEditor extends GraphBuilder {

    public static final ArrayList<Node> selectedNodes = new ArrayList<>();
    public static final ArrayList<Node> lockedNodes = new ArrayList<>();
    public static final ArrayList<Edge> adjacentEdges = new ArrayList<>();
    public static float totalArea = 0;
    protected static App p5;
    protected static Edge activeEdge;
    protected static Node activeNode;

    public GraphEditor(App $p5) {
        p5 = $p5;
    }

    public static void lockNode() {
        if (lockedNodes.contains(getActiveNode())) {
            lockedNodes.remove(getActiveNode());
            getActiveNode().particle2D.unlock();
        } else {
            getActiveNode().particle2D.lock();
            lockedNodes.add(getActiveNode());
        }
    }

    public static void moveNode(Vec2D mousePos) {
        getActiveNode().particle2D.lock();
        getActiveNode().particle2D.set(mousePos);
    }

    public static void selectNodeNearPosition(Vec2D mousePos) {
        if (!Keyboard.isShiftDown) {
            selectedNodes.clear();
            adjacentEdges.clear();
        } else if (hasActiveNode()) {
            selectedNodes.add(getActiveNode());
            deselectNode();
        }
        for (Node n : nodes) {
            Rect r = new Rect(p5.width - 200, 50 + (n.getId() * 14), 160, 12);
            Circle c = new Circle(n.getX(), n.getY(), 20);

            if (c.containsPoint(mousePos)) {
                setActiveNode(n);
                selectAdjacentEdges(n);
                break;
            } else if (r.containsPoint(mousePos)) {
                setActiveNode(n);
                selectAdjacentEdges(n);
                break;
            } else {
                deselectNode();
            }
        }
    }

    public static int totalNodeArea() {
        float totalNodeArea = 0;

        for (Node n : nodes) {
            totalNodeArea += n.getSize();
        }
        return (int) Math.abs(totalNodeArea);
    }

    public static void addDuplicate(float num, boolean split) {
        if (hasActiveNode()) {
            Node parent = getActiveNode();
            float size = parent.getSize();
            for (int i = 1; i <= num; i++) {
                Vec2D pos = Vec2D.fromTheta(i * App.TWO_PI / num).scaleSelf(size).addSelf(parent.particle2D);
                Node child = new Node(parent.getName() + i, size, pos);
                createNode(child);
                updateGraph();
                createEdge(new Edge(parent, child));
                selectedNodes.add(child);
            }
            if (split) parent.setSize(size / (num + 1));
        }
    }

    public static boolean hasActiveNode() {
        return getActiveNode() != null;
    }

    public static Node getActiveNode() {
        return activeNode;
    }

    public static void setActiveNode(Node n) {
        activeNode = n;
        selectAdjacentEdges(n);
    }

    public static void deselectNode() {
        releaseNode();
        setActiveNode(null);
        adjacentEdges.clear();
    }

    protected static void selectAdjacentEdges(Node n) {
        deselectEdges();
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getA() == n) {
                adjacentEdges.add(e);
                continue;
            }
            if (e.getB() == n) {
                adjacentEdges.add(e);
            }
        }
        adjacentEdges.addAll(adjacentEdges);
    }

    public static void releaseNode() {
        if (hasActiveNode()) {
            if (!lockedNodes.contains(getActiveNode())) {
                getActiveNode().particle2D.unlock();
            }
        }
    }

    protected static void deselectEdges() {
        adjacentEdges.clear();
    }

    public static Edge getActiveEdge() {
        return activeEdge;
    }

    public void clearGraph() {
        Map = new XmlMap();
        nodes.clear();
        edges.clear();
    }

    public void delete(String mode) {
        switch (mode) {
            case "edge":
                removeEdge(getActiveEdge());
                break;
            case "edges":
                for (Edge e : adjacentEdges) {
                    removeEdge(e);
                }
                deselectEdges();
                break;
            case "node":
                deleteNode(getActiveNode());
                break;
            case "nodes":
                for (Node n : selectedNodes) {
                    deleteNode(n);
                }
                break;
        }
    }

    public void deleteNode(Node n) {
        if (n != null) {
            while (hasActiveNode()) {
                ArrayList<Edge> activeEdges = new ArrayList<>();
                for (Edge e : edges) {
                    if (e.getA() == n) {
                        activeEdges.add(e);
                        continue;
                    }
                    if (e.getB() == n) {
                        activeEdges.add(e);
                    }
                }
                for (Edge e : activeEdges) {
                    removeEdge(e);
                }
                removeNode(n);
                selectedNodes.clear();
                adjacentEdges.clear();
                deselectNode();
            }
        }
    }

    public void makeEdge() {
        if (hasActiveNode()) {
            for (Node n : selectedNodes) {
                if (getActiveNode() == n) return;
                if (getEdge(n, getActiveNode()) == null) {
                    createEdge(new Edge(n, getActiveNode()));
                }
            }
        }
    }

    public void makeNode(String name, float size, Vec2D pos) {
        createNode(new Node(name, size, pos));
    }

    public void scaleNode(float size) {
        if (hasActiveNode()) getActiveNode().setSize(size);
        for (Node g : selectedNodes) {
            g.setSize(size);
        }
    }
}

/*	public static void setActiveEdge(Edge activeEdge) {
        GraphEditor.activeEdge = activeEdge;
	}*/
//	public static float getActiveSize() {return activeNode.getSize();}
//	public static Node getHoveredNode() {		return hoveredNode;	}
//	public static void setActiveSize(float activeSize) { activeSize = activeSize; }
//	protected static void clearSelection() { }
    /*	public static GraphEditor getInstance(App p5) {
            if (instance == null) { instance = new GraphEditor(p5); lock = true; }
			return instance;
		}*/
/*	public static void highlightNodeNearPosition(Vec2D mousePos) {
		for (Node n : nodes) {
			if (n.rollover(mousePos)) { hoveredNode = n; break; }
			else { hoveredNode = null; }
		}
	}*/
//	public static void setHoveredNode(Node hoveredNode) { GraphEditor.hoveredNode = hoveredNode; }
	/*	 GraphEditor(App $p5) {
			p5 = $p5;
			if (lock) { throw new Error("Error: GraphEditor Instantiation failed. Use getInstance() instead of new."); }
			else { System.out.println("GraphEditor Instantiated Successfuly"); }
		}*/  //	public static    Node    hoveredNode;
//	protected static boolean lock;
//	private static GraphEditor instance = null;
//	public static    float   activeSize;
