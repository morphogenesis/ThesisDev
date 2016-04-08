package core.graph.map;

import core.App;
import toxi.physics2d.VerletSpring2D;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Created on 2/13/14.
 */
//@XmlRootElement(name = "rel")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "edge", propOrder = {"from", "to"})
public class Edge {

    @XmlAttribute
    public int from = 0;
    @XmlAttribute
    public int to = 0;

    public float length = 0.0F;

    public Node a = null;
    public Node b = null;

    public VerletSpring2D spring2D = null;

    public Edge() {
    }

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
        this.from = (a.getId());
        this.to = (b.getId());
        this.length = a.getRadius() + b.getRadius();
        this.spring2D = new VerletSpring2D(a.particle2D, b.particle2D, getLength(), 1);
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void update() {

        length = a.getRadius() + b.getRadius();
        spring2D.setStrength(App.PHYSICS.phys_spr_strength);
        spring2D.setRestLength(length * App.PHYSICS.phys_spr_scale);
    }

    public Node getA() {
        return a;
    }

    public void setA(Node nA) {
        this.a = nA;
    }

    public Node getB() {
        return b;
    }

    public void setB(Node nB) {
        this.b = nB;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public VerletSpring2D getSpring2D() {
        return spring2D;
    }

    public void setSpring2D(VerletSpring2D s) {
        this.spring2D = s;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
    /**
     * Created on 3/10/14.
     */

}
