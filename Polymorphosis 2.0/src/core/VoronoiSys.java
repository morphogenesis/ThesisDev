package core;

import core.Metaball.Metaball;
import core.graph.GraphBuilder;
import core.graph.Physics;
import core.graph.map.Node;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import toxi.geom.ConvexPolygonClipper;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletSpring2D;

import java.util.ArrayList;
import java.util.List;


public class VoronoiSys {
    private static Polygon2D bounds = Physics.physics.getWorldBounds().toPolygon2D();
    private static ConvexPolygonClipper clipper = new ConvexPolygonClipper(bounds);
    private static ArrayList<Polygon2D> interiorRegions = new ArrayList<>();
    private static Voronoi voronoi = new Voronoi();
    private final App p5;
    public boolean update_voronoi = false;
    public boolean draw_voronoi = false;
    public boolean draw_vor_poly = false;
    public boolean draw_vor_offset = false;
    public boolean draw_vor_bez = false;
    public boolean draw_vor_vec = false;
    public boolean draw_vor_info = false;
    public boolean draw_vor_metaball = true;
    public float vor_offset = 0.0F;
    public float vor_meta_res = 50;
    private List<Vec2D> sites = new ArrayList<>();

    public VoronoiSys(App p5) {
        this.p5 = p5;
    }

    public static ArrayList<Polygon2D> getInteriorRegions() {
        return interiorRegions;
    }

    public void update() {
        if (draw_vor_metaball) {
            Polygon2D p = new Polygon2D(Metaball.getPoints());
            p.removeDuplicates(vor_meta_res);
            while (p.getNumVertices() > 100) p.removeDuplicates(++vor_meta_res);
            sites = p.vertices;
        }
        if (update_voronoi) {
            voronoi = new Voronoi();
            for (Node n : GraphBuilder.nodes) voronoi.addPoint(n.particle2D);
            for (Vec2D v : sites) voronoi.addPoint(v);
        }
    }

    public void drawParticle(VerletParticle2D particle, float radius, int stroke, int fill) {
        updateColor(stroke, fill);
        p5.ellipse(particle.x, particle.y, radius, radius);
    }

    public void drawSprings(VerletSpring2D spring, int stroke, int fill, App app) {
        p5.style(stroke, fill);
        p5.line(spring.a.x, spring.a.y, spring.b.x, spring.b.y);
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

    public void draw() {
        if (draw_vor_info) {
            p5.textFont(App.pfont10, 10);
            interiorRegions = new ArrayList<>();

            List<Polygon2D> regions = voronoi.getRegions();
            for (Polygon2D poly : regions) {
                poly = clipper.clipPolygon(poly);
                if (poly.vertices.size() < 3) return;
                if (!poly.isClockwise()) poly.flipVertexOrder();
                if (draw_vor_offset) poly.offsetShape(vor_offset);
                if (draw_vor_poly) drawPoly(poly, 0x33222222, -1);

                for (Node n : GraphBuilder.nodes) {
                    if (poly.containsPoint(n.particle2D)) {
                        interiorRegions.add(poly);
                        if (draw_vor_poly) drawPoly(poly, 0xff222222, 0x66222222);
                        if (draw_vor_bez) drawPolyBezier(poly, 0xff666666, 0x66222222);
                        if (draw_vor_vec) drawPolyVerts(poly, -1, 0xffeca860);
                    }
                }
//			if (draw_vor_info) { drawPoly(poly, 0xff222222, -1); drawRegionInfo(poly, i, 0xff666666); }
            } /*if (draw_vor_info) {
            for (Vec2D v : voronoi.getSites()) { drawSiteInfo(v, voronoi.getSites().indexOf(v), 0xff33ffff); }
		}*/
        }
    }

    void drawMetaSites() {
        if (draw_vor_info) {
            for (Vec2D v : sites) {
                p5.stroke(0x33888888);
                p5.fill(0x88111111);
                p5.circle(v, 2);
            }
        }
    }

    public void clear() {
        voronoi = new Voronoi();
        clipper = new ConvexPolygonClipper(bounds);
        interiorRegions = new ArrayList<>();
    }

    public int getNumMetaSites() {
        return sites.size();
    }

    public String[] toStringArray() {
        return new String[]{
                "VORONOI",
                "vor_meta_res         " + vor_meta_res,
                "vor_offset           " + vor_offset,
                "update_voronoi       " + update_voronoi,
                "draw_voronoi         " + draw_voronoi,
                "draw_vor_poly        " + draw_vor_poly,
                "draw_vor_offset      " + draw_vor_offset,
                "draw_vor_bez         " + draw_vor_bez,
                "draw_vor_vec         " + draw_vor_vec,
                "draw_vor_info        " + draw_vor_info,
                "draw_vor_metaball    " + draw_vor_metaball
        };
    }

    protected void drawPoly(Polygon2D poly, int stroke, int fill) {
        updateColor(stroke, fill);
        for (Line2D l : poly.getEdges()) {
            p5.line(l.a.x, l.a.y, l.b.x, l.b.y);
        }
    }

    protected void drawPolyBezier(Polygon2D poly, int stroke, int fill) {
        updateColor(stroke, fill);

        List<Vec2D> list = poly.vertices;
        Vec2D a = list.get(0);
        Vec2D b = list.get(list.size() - 1);
        Vec2D o = new Vec2D((b.x + a.x) / 2, (b.y + a.y) / 2);

        p5.beginShape();
        p5.vertex(o.x, o.y);
        for (int i = 0; i < list.size(); i++) {
            Vec2D c = list.get(i);
            Vec2D d = list.get((i + 1) % list.size());
            p5.bezierVertex(c.x, c.y, c.x, c.y, (d.x + c.x) / 2, (d.y + c.y) / 2);
        }
        p5.endShape(PApplet.CLOSE);
    }

    protected void drawPolyVerts(Polygon2D poly, int stroke, int fill) {
        updateColor(stroke, fill);
        for (Vec2D v : poly.vertices) {
            p5.ellipse(v.x, v.y, 1, 1);
        }
    }

    protected void drawSiteInfo(Vec2D v, int index, int fill) {
        p5.fill(fill);
        p5.textAlign(PConstants.RIGHT);
        p5.text(index, v.x - 10, v.y);
        p5.noFill();
    }

    protected void drawRegionInfo(Polygon2D poly, int index, int fill) {
        float x = poly.getCentroid().x + 10;
        float y = poly.getCentroid().y;
        p5.fill(fill);
        p5.textAlign(PConstants.LEFT);
        p5.text("Index: " + index, x, y);
        p5.text("Area: " + (int) (poly.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE)), x, y + 10);
        p5.text("Circ: " + (int) poly.getCircumference() / App.WORLD_SCALE, x, y + 20);
        p5.text("Verts: " + poly.getNumVertices(), x, y + 30);
        p5.noFill();
    }

    protected void drawShape(Polygon2D poly) {
        int id = 0;
        PShape pShape = p5.createShape();
        pShape.beginShape();
        pShape.fill(0xff444444);
        pShape.stroke(0xffffffff);
        for (Vec2D v : poly.vertices) {
            pShape.vertex(v.x, v.y);
            p5.fill(255);
            p5.text(id++, v.x, v.y);
        }
        pShape.endShape(PConstants.CLOSE);
        p5.shape(pShape);
    }
}
