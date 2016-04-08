package core.graph.map;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flowgraph", propOrder = {"nodes", "edges"})
public class XmlMap {

    @XmlElement(name = "node", nillable = true)
    protected List<Node> nodes = null;

    @XmlElement(name = "edge", nillable = true)
    protected List<Edge> edges = null;

    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        return this.nodes;
    }

    public List<Edge> getEdges() {
        if (edges == null) {
            edges = new ArrayList<>();
        }
        return this.edges;
    }

    //	public void setNodes(ArrayList<Node> nodes) { this.nodes = nodes; }
    //	public void setEdges(ArrayList<Edge> edges) { this.edges = edges; }
}
