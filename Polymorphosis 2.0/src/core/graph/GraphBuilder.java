package core.graph;

import core.App;
import core.Info;
import core.graph.map.Edge;
import core.graph.map.Node;
import core.graph.map.XmlMap;
import core.gui.Display;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class GraphBuilder {

    public static ArrayList<Node> nodes = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();
    protected static XmlMap Map = new XmlMap();
    private static HashMap<Integer, Node> nodeIndex = new HashMap<>();
    private static HashMap<Integer, ArrayList<Node>> edgeIndex = new HashMap<>();

    public static void openXML(File file) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        edgeIndex = new HashMap<>();
        nodeIndex = new HashMap<>();

        App.PHYSICS.clear();
        App.METABALL.clear();
        Display.totalArea = 0;

        xml_unmarshal(file);
    }

    private static void xml_unmarshal(File file) {
        System.out.println("READING XML FROM:" + file.getPath());

        XmlMap map;

        try {
            JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
            Unmarshaller um = jc.createUnmarshaller();
            map = (XmlMap) um.unmarshal(new File(Info.tmpFilepath));

            if (map.getNodes() != null) {
                for (Node n : map.getNodes()) {
                    createNode(n);
                }
            }
            if (map.getEdges() != null) {
                for (Edge e : map.getEdges()) {
                    createEdge(e);
                }
            }
            Map = map;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    protected static void createNode(Node n) {
        Display.totalArea += n.getSize();
        n.setParticle2D(new VerletParticle2D(n.getX(), n.getY(), App.PHYSICS.phys_vec_wght));
        n.setBehavior2D(new AttractionBehavior2D(n.particle2D, n.getRadius(), App.PHYSICS.phys_bhavior_str));
        nodes.add(n);
        n.setId(nodes.indexOf(n));
        nodeIndex.put(n.getId(), n);
        App.PHYSICS.addParticle(n);
    }

    protected static void createEdge(Edge e) {

        if (getEdge(e.getA(), e.getB()) == null) {
            e.setA(getNode(e.getFrom()));
            e.setB(getNode(e.getTo()));
            e.setSpring2D(new VerletSpring2D(e.getA().particle2D, e.getB().particle2D, e.getLength(), 1));

            edges.add(e);

            ArrayList<Node> relatives = edgeIndex.get(e.getFrom());
            if (relatives == null) {
                relatives = new ArrayList<>();
                edgeIndex.put(e.getFrom(), relatives);
            }
            relatives.add(getNode(e.getTo()));

            App.PHYSICS.addSpring(e);

            System.out.println("createEdge(Edge e)");
        }
    }

    protected static Node getNode(int id) {
        return nodeIndex.get(id);
    }

    protected static Edge getEdge(Node a, Node b) {
        for (Edge e : edges) {
            if ((e.getA() == a && e.getB() == b) || (e.getA() == b && e.getB() == a)) {
                return e;
            }
        }
        return null;
    }

    public static void saveXML(File file) {
        updateGraph();
//		File newfile = new File(file.getPath());
        System.out.println();
        System.out.println("WRITING XML TO: " + file.getPath());
        try {
            JAXBContext jc = JAXBContext.newInstance(XmlMap.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(Map, System.out);
            m.marshal(Map, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println("SAVED TO: " + file.getPath());
    }

    public static void updateGraph() {
        Map = new XmlMap();
        Map.getNodes().addAll(nodes);
        Map.getEdges().addAll(edges);
        edgeIndex = new HashMap<>();
        nodeIndex = new HashMap<>();
        for (Node n : nodes) {
            Display.totalArea += n.getSize();
            int id = nodes.indexOf(n);
            n.setId(id);
            nodeIndex.put(id, n);
        }
        for (Edge e : edges) {
            ArrayList<Node> relatives = edgeIndex.get(e.getFrom());
            if (relatives == null) {
                relatives = new ArrayList<>();
                edgeIndex.put(e.getFrom(), relatives);
            }
            relatives.add(getNode(e.getTo()));
        }
    }

    protected void removeEdge(Edge edge) {
//		VerletSpring2D spring = edge.getSpring2D();
        edges.remove(edge);
        edgeIndex.clear();
        for (Edge e : edges) {
            ArrayList<Node> relatives = edgeIndex.get(e.getFrom());
            if (relatives == null) {
                relatives = new ArrayList<>();
                edgeIndex.put(e.getFrom(), relatives);
            }
            relatives.add(getNode(e.getTo()));
        }

//		Physics.springs.remove(spring);
//		Physics.physics.removeSpring(edge.getSpring2D());
        App.PHYSICS.removeSpring(edge);
    }

    protected void removeNode(Node n) {
        nodes.remove(n);
        nodeIndex.remove(n.getId());
        for (Node i : nodes) {
            i.setId(nodes.indexOf(i));
        }
        App.PHYSICS.removeParticle(n);
        App.METABALL.removeMetaball(n.ball);
    }
}

