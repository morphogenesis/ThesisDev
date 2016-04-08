package core.graph.map;

import core.App;
import core.Info;
import core.Metaball.Ball;
import core.tools.Vector2D;
import toxi.geom.Circle;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Created on 3/11/14.
 */ //@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "node", propOrder = {"id", "name", "size", "x", "y", "type", "occupancy"})
public class Node {

    @XmlAttribute
    public int id = 0;
    @XmlAttribute
    public String name = null;
    @XmlAttribute
    public float size = 0.0F;
    @XmlAttribute
    public float x = 0.0F;
    @XmlAttribute
    public float y = 0.0F;
    @XmlAttribute
    public int occupancy = 0;
    @XmlAttribute
    public int type = 0;

    public float metaballScale = 1;
    public float radius = 1;
    public VerletParticle2D particle2D;
    public AttractionBehavior2D behavior2D;
    public Ball ball;
    Circle circle = null;
    boolean isActive = false;
    boolean isSelected = false;
    boolean isLocked = false;

    public Node() {
        this.radius = (float) (Math.sqrt(this.size / Math.PI));
        this.particle2D = new VerletParticle2D(Info.WIDTH / 2, Info.HEIGHT / 2);
        this.behavior2D = new AttractionBehavior2D(particle2D, radius, -1);
        this.ball = new Ball(this.x, this.y, getRadius());
        this.circle = new Circle(particle2D, getRadius());
        ball.strength(getRadius() / App.WORLD_SCALE);
        App.METABALL.addMetaball(this.ball);
        particle2D.set(x, y);
        //		this.id = ++numberOfGNodes;
    }

    public Node(String name, float size, Vec2D pos) {
        //		this.id = ++numberOfGNodes;
        this.name = name;
        this.size = size;
        this.x = pos.x;
        this.y = pos.y;
        particle2D = new VerletParticle2D(pos);
        behavior2D = new AttractionBehavior2D(particle2D, getRadius(), -1);
        ball = new Ball(pos.x, pos.y, getRadius() / App.WORLD_SCALE);
        //		App.PHYSICS.addParticle(this);
        App.METABALL.addMetaball(this.ball);
    }

    public float getRadius() {
        return (float) ((Math.sqrt(this.size / Math.PI)) * App.PHYSICS.phys_vec_scale * App.WORLD_SCALE);
    }
    //	public static int getNumberOfGNodes() {return numberOfGNodes;}
    //	public static void setNumberOfGNodes(int $numberOfGNodes) { numberOfGNodes = $numberOfGNodes; }

    public void update() {
        x = (particle2D.x);
        y = (particle2D.y);
        particle2D.setWeight(App.PHYSICS.phys_vec_wght);
        behavior2D.setRadius((getRadius() * App.PHYSICS.phys_bhavior_scale) * App.WORLD_SCALE);
        behavior2D.setStrength(App.PHYSICS.phys_bhavior_str);
        ball.position(new Vector2D(x, y));
        ball.strength((getRadius() / App.WORLD_SCALE) + metaballScale);
    }

    public void display(App p5) {
        if (rollover(p5.mousePos())) {
            p5.circle(particle2D, getRadius() + 10, 0xffff0000);
        }
    }

    public AttractionBehavior2D getBehavior2D() {
        return behavior2D;
    }

    public void setBehavior2D(AttractionBehavior2D behavior2D) {
        this.behavior2D = behavior2D;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setParticle2D(VerletParticle2D particle2D) {
        this.particle2D = particle2D;
    }

    public boolean rollover(Vec2D mousePos) {
        circle = new Circle(particle2D.x, particle2D.y, getRadius());

        return circle.containsPoint(mousePos);
    }

    public void toggleState() {
        isActive = !isActive;
    }

    @Override
    public String toString() {
        return "Node{" +
                "metaballScale=" + metaballScale +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", occupancy=" + occupancy +
                ", x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}
