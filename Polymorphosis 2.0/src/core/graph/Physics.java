package core.graph;

import core.App;
import core.graph.map.Edge;
import core.graph.map.Node;
import toxi.geom.Rect;
import toxi.physics2d.VerletMinDistanceSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import java.util.ArrayList;
import java.util.List;


public final class Physics {
    public static VerletPhysics2D physics = new VerletPhysics2D();
    public static ArrayList<VerletParticle2D> particles = new ArrayList<>();
    public static List<VerletSpring2D> mindists = new ArrayList<>();
    public static List<VerletSpring2D> springs = new ArrayList<>();
    public static ArrayList<VerletParticle2D> attractorParticles = new ArrayList<>();
    public static List<AttractionBehavior2D> behaviors = new ArrayList<>();
    public static ArrayList<VerletSpring2D> attractorMin = new ArrayList<>();
    public static Rect bounds;
    public boolean update_physics = true;
    public boolean draw_physics = true;
    public boolean draw_phys_info = false;
    public float phys_drag = 0.3f;
    public float phys_vec_scale = 1;
    public float phys_vec_wght = 1f;
    public float phys_spr_scale = 1;
    public float phys_spr_strength = 0.01f;
    public float phys_bhavior_str = 2f;
    public float phys_bhavior_scale = -1f;
    public float phys_mindist_str = 0.01f;
    private App p5 = null;

    public Physics(App p5) {
        this.p5 = p5;
//		graphicsObject = new MyGraphicsObject(p5);
        bounds = new Rect(10, 10, p5.width - 20, p5.height - 20);
        physics.setWorldBounds(bounds);
        physics.setDrag(phys_drag);
    }

    public void draw() {

        if (update_physics) {
            update();
        }
        if (draw_phys_info) {
            for (VerletParticle2D a : attractorParticles) {
                drawParticle(a, 0xff1d1d1d, 0xff1d1d1d);
            }
            for (VerletParticle2D a : particles) drawParticle(a, a.getWeight(), 0xcc000000);
        }
        if (draw_physics) {
            for (VerletSpring2D s : physics.springs) p5.line(s, 0xff1d1d1d);
            for (VerletParticle2D a : physics.particles) p5.circle(a, 3, 0xff1d1d1d, 0xff1d1d1d);
        }
    }

    void update() {
        physics.update();
        physics.setDrag(phys_drag);
    }

    public void drawParticle(VerletParticle2D particle, float radius, int stroke) {
        p5.circle(particle, radius, stroke);
//		updateColor(stroke, fill);
//		p5.ellipse(particle.x, particle.y, radius, radius);
    }

    public void updateColor(int stroke, int fill) {
        if (fill == -1) {
            p5.noFill();
        } else {
            p5.fill(fill);
        }
        if (stroke == -1) {
            p5.noStroke();
        } else {
            p5.stroke(stroke);
        }
    }

    public void addMinDist() {
        clearMinDist();
        for (Node na : GraphBuilder.nodes) {
            VerletParticle2D va = na.particle2D;
            for (Node nb : GraphBuilder.nodes) {
                VerletParticle2D vb = nb.particle2D;
                if ((na != nb) && (physics.getSpring(na.particle2D, nb.particle2D) == null)) {
                    float len = (na.getRadius() + nb.getRadius());
                    VerletSpring2D s = new VerletMinDistanceSpring2D(va, vb, len, .01f);
                    mindists.add(s);
                    physics.addSpring(s);
                }
            }
        }
    }

    public void clearMinDist() {
        for (VerletSpring2D s : attractorMin) {
            physics.springs.remove(s);
        }
        for (VerletSpring2D s : mindists) {
            physics.springs.remove(s);
        }
        attractorMin.clear();
        mindists.clear();
    }

    public void addParticle(Node n) {
        particles.add(n.particle2D);
        physics.addParticle(n.particle2D);
        behaviors.add(n.getBehavior2D());
        physics.addBehavior(n.getBehavior2D());
    }

    public void addSpring(Edge e) {
        springs.add(e.getSpring2D());
        physics.addSpring(e.getSpring2D());
    }

    public void clear() {
        springs.clear();
        mindists.clear();
        particles.clear();
        behaviors.clear();
        physics.clear();
    }

    public void drawSprings(VerletSpring2D spring, int stroke, int fill, App app) {
        updateColor(stroke, fill);
        p5.line(spring.a.x, spring.a.y, spring.b.x, spring.b.y);
    }

    public void removeParticle(Node n) {
        particles.remove(n.particle2D);
        physics.removeParticle(n.particle2D);
        behaviors.remove(n.getBehavior2D());
        physics.removeBehavior(n.getBehavior2D());
    }

    public void removeSpring(Edge e) {
        springs.remove(e.getSpring2D());
        physics.removeSpring(e.getSpring2D());
    }

    public String[] toStringArray() {
        return new String[]{"PHYSICS:",
                "update_physics       " + update_physics,
                "draw_physics         " + draw_physics,
                "draw_phys_info       " + draw_phys_info,
                "phys_drag            " + phys_drag,
                "phys_vec_scale       " + phys_vec_scale,
                "phys_vec_wght        " + phys_vec_wght,
                "phys_spr_scale       " + phys_spr_scale,
                "phys_spr_strength    " + phys_spr_strength,
                "phys_bhavior_str     " + phys_bhavior_str,
                "phys_bhavior_scale   " + phys_bhavior_scale,
                "phys_mindist_str     " + phys_mindist_str};
    }
}
//	public static ArrayList<Attractor> attractors = new ArrayList<>();

/*	public void build() {
*//*		physics.clearGraph();
        for (Node n : GraphBuilder.nodes) {
			VerletParticle2D p = new VerletParticle2D(n.x, n.y, phys_vec_wght);
			n.setParticle2D(p);
			physics.addParticle(p);
			AttractionBehavior2D a = new AttractionBehavior2D(p, n.getRadius() * phys_bhavior_scale, phys_bhavior_str);
			n.setBehavior2D(a);
			physics.addBehavior(a);
		}
		for (Edge e : GraphBuilder.edges) {
			VerletSpring2D s = new VerletSpring2D(e.getA().getParticle2D(), e.getB().getParticle2D(), e.getA().getRadius() + e.getB().getRadius(), phys_spr_strength);
			e.setSpring2D(s);
			physics.addSpring(s);
		}*//*
    }*/
/*	*/
/*	public void addAttractor(Attractor attractor) {
		attractors.add(attractor);
		physics.addParticle(attractor);
		physics.addBehavior(new AttractionBehavior2D(attractor, phys_bhavior_scale, phys_bhavior_str));
	}
	public class Attractor extends VerletParticle2D {

		float r = 1;
		public Attractor(float x, float y, float r) {
			super(x, y);
			this.r = r;
			physics.addParticle(this);
			physics.addBehavior(new AttractionBehavior2D(this, phys_bhavior_scale, phys_bhavior_str));
		}
		public void display(App $p5) {
			$p5.fill(0xff666666);
			$p5.stroke(0);
			$p5.circle(x, y, r * 2, r * 2);
		}
	}*/
/*		for (Node n : GraphEditor.nodes) {
			n.getParticle2D().setWeight(phys_vec_wght);
			n.getBehavior2D().setRadius(n.getRadius() * phys_bhavior_scale);
			n.getBehavior2D().setStrength(phys_bhavior_str);
		}*/
	/*	for (Edge e : GraphEditor.edges) {
			e.getSpring2D().setStrength(phys_spr_strength);
			e.getSpring2D().setRestLength(e.getA().getRadius() + e.getB().getRadius() * phys_spr_scale);
		}		for (VerletSpring2D s : springs) { s.setStrength(phys_spr_strength); }
*/
//			for (AttractionBehavior2D a : behaviors) {drawAttractor(a, 0xff343434, -1); }
//			for (Attractor a : attractors) {drawAttractor(a, 0xff222222, -1); }
//			for (VerletSpring2D s : mindists) drawSprings(s, 0xff333333, -1, p5);

//		for (AttractionBehavior2D b : behaviors) {/*b.setRadius(phys_bhavior_scale);*/b.setStrength(phys_bhavior_str);}
//		for (VerletSpring2D s : mindists) {s.setStrength(phys_mindist_str);}
