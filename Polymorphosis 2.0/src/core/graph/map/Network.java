package core.graph.map;

import core.App;
import core.tools.Color;
import toxi.geom.Circle;
import toxi.geom.Line2D;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;


/**
 * Created on 3/10/14.
 */
public class Network extends XmlMap {
    private VerletParticle2D particle;

    public RGBspring makeLine(VerletSpring2D spring) {
        return new RGBspring(spring);
    }

    public RGBparticle makeCircle(VerletParticle2D particle) {
        this.particle = particle;
        return new RGBparticle(particle);
    }

    static class RGBspring {
        Line2D line = null;
        boolean isActive = false;
        boolean isSelected = false;

        public RGBspring(VerletSpring2D spring) {
            this.line = new Line2D(spring.a, spring.b);
            //			Physics.physics.addSpring(this);
        }

        public void draw(App p5) {
            p5.stroke(isActive ? Color.ACTIVE : (isSelected ? Color.SELECTED : Color.STANDARD));
            p5.line(line.a.x, line.a.y, line.b.x, line.b.y);
        }
    }

    static class RGBparticle {
        VerletParticle2D particle = null;

        Circle circle = null;
        float radius = 0.0f;
        boolean isActive = false;
        boolean isSelected = false;
        boolean isLocked = false;

        public RGBparticle(VerletParticle2D particle) {
            this.circle = new Circle(particle, radius);
        }

        public void draw(App p5) {
            p5.stroke(isActive ? Color.ACTIVE : (isSelected ? Color.SELECTED : Color.STANDARD));
            p5.circle(circle, circle.getRadius());
        }

        public boolean rollover(Vec2D mousePos) {
            return circle.containsPoint(mousePos);
        }

        public void toggleState() {
            isActive = !isActive;
        }
    }
}
