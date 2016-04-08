package core.Metaball;

import core.App;
import core.tools.Vector2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;

import java.util.ArrayList;
import java.util.List;


public class Metaball {
    public static ArrayList<Ball> _balls;
    protected static boolean lock;
    protected static float minStrength;
    private static Metaball instance = null;
    private static ArrayList<Vec2D> points = new ArrayList<>();
    private static ArrayList<Polygon2D> polygons = new ArrayList<>();
    private final App p5;
    /*	public float getMeta_viscosity() {
            return meta_viscosity;
        }
        public void setMeta_viscosity(float meta_viscosity) {
            this.meta_viscosity = meta_viscosity;
        }*/
    public float meta_viscosity = 2;
    public float meta_threshold = 0.0006f;
    public float meta_resolution = 40;
    public int meta_maxSteps = 900;
    public boolean draw_metaball = false;
    public boolean draw_meta_info = false;
    public float meta_maxStrength = 200;

    private Metaball(App p5) {
        this.p5 = p5;
        if (lock) {
            throw new Error("Error: Instantiation failed. Use getInstance() instead of new.");
        } else {
            _balls = new ArrayList<>();
            minStrength = Ball.MIN_STRENGTH;
        }
    }

    public static Metaball getInstance(App p5) {
        if (instance == null) {
            instance = new Metaball(p5);
            lock = true;
        }
        return instance;
    }

    public static ArrayList<Vec2D> getPoints() {
        return points;
    }

    public static ArrayList<Polygon2D> getPolygons() {
        return polygons;
    }

    public void draw() {
        p5.textFont(App.pfont10, 10);
        if (_balls.size() > 1) {
            run();

            if (draw_metaball) {
                p5.stroke(0x33888888);
                p5.fill(0x88111111);
                for (Vec2D v : points) {
                    p5.circle(v, 2);
                }
            }
        }
    }

    public void run() {
        Ball.MAX_STRENGTH = meta_maxStrength;
        Vector2D seeker = new Vector2D();
        int i;

        for (Ball ball : _balls) {
            ball.tracking = false;
            seeker.copy(ball.position());
            i = 0;
            while ((stepToEdge(seeker) > meta_threshold) && (++i < 50)) {
            }
            ball.edge.copy(seeker);
        }

        int edgeSteps = 0;
        Ball current = untrackedMetaball();
        points = new ArrayList<>();

        seeker.copy(current.edge);
        while (current != null && edgeSteps < meta_maxSteps) {
            Vec2D v1 = new Vec2D(seeker.x, seeker.y);
            rk2(seeker, meta_resolution);
            Vec2D v2 = new Vec2D(seeker.x, seeker.y);
            if (draw_meta_info) p5.line(v1, v2, 0x22888888);
            points.add(v2);

            for (Ball ball : _balls) {
                if (seeker.dist(ball.edge) < (meta_resolution * 0.999f)) {
                    seeker.copy(ball.edge);
                    current.tracking = true;
                    if (ball.tracking) {
                        current = untrackedMetaball();
                        if (current != null) {
                            seeker.copy(current.edge);
                        }
                    } else {
                        current = ball;
                    }
                    break;
                }
            }
            ++edgeSteps;
        }
    }

    protected void rk2(Vector2D v, float h) {
        Vector2D t1 = calc_normal(v).getPerpLeft();
        t1.multiply(h * 0.5f);
        Vector2D t2 = calc_normal(Vector2D.add(v, t1)).getPerpLeft();
        t2.multiply(h);
        v.add(t2);
    }

    protected float stepToEdge(Vector2D seeker) {
        float force = calc_force(seeker);
        float stepsize;
        stepsize = (float) Math.pow(minStrength / meta_threshold, 1 / meta_viscosity) - (float) Math.pow(minStrength / force, 1 / meta_viscosity) + 0.01f;
        seeker.add(calc_normal(seeker).multiply(stepsize));
        return force;
    }

    private Vector2D calc_normal(Vector2D v) {
        Vector2D force = new Vector2D();
        Vector2D radius;
        for (Ball ball : _balls) {
            radius = Vector2D.subtract(ball.position(), v);
            if (radius.getLengthSq() == 0) {
                continue;
            }
            radius.multiply(-meta_viscosity * ball.strength() * (1 / (float) Math.pow(radius.getLengthSq(), (2 + meta_viscosity) * 0.5f)));
            force.add(radius);
        }
        return force.norm();
    }

    private float calc_force(Vector2D v) {
        float force = 0.0f;
        for (Ball ball : _balls) {
            force += ball.strengthAt(v, meta_viscosity);
        }
        return force;
    }

    protected Ball untrackedMetaball() {
        for (Ball ball : _balls) {
            if (!ball.tracking) {
                return ball;
            }
        }
        return null;
    }

    public void addMetaball(Vector2D pos) {
        Ball m = new Ball(pos, p5.random(1, 4));
        addMetaball(m);
    }

    public void addMetaball(Ball ball) {
        minStrength = Math.min(ball.strength(), minStrength);
        _balls.add(ball);
//		meta_maxSteps = _balls.size() * 40;
    }

    public void clear() {
        _balls.clear();
    }

    public List<Vec2D> get_vertices() {
        ArrayList<Vec2D> vertices = new ArrayList<>();
        for (Polygon2D p : polygons) {
            vertices.addAll(p.vertices);
        }
        return vertices;
    }

    public void removeMetaball(Ball ball) {
        _balls.remove(ball);
    }

    public String[] toStringArray() {
        return new String[]{"METABALL",
                "size                 " + getSize(),
                "meta_viscosity       " + meta_viscosity,
                "meta_threshold       " + meta_threshold,
                "meta_resolution      " + meta_resolution,
                "meta_maxSteps        " + meta_maxSteps,
                "meta_maxStrength     " + meta_maxStrength,
                "draw_metaball        " + draw_metaball,
                "draw_meta_info       " + draw_meta_info
        };
    }

    public int getSize() {
        return _balls.size();
    }
}
