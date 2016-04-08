package core.graph;

import core.App;
import core.Info;
import core.Metaball.Ball;
import core.graph.map.Edge;
import core.graph.map.Node;
import core.tools.StringUtil;
import processing.core.PApplet;
import toxi.geom.Vec2D;

import static core.tools.Color.*;


/**
 * Created on 3/11/14.
 */
public class GraphRenderer extends GraphEditor {
    public static boolean update_graph;
    public static boolean draw_graph;
    public static boolean draw_graph_info;
    public static boolean draw_graph_list;
    public static boolean draw_graph_nodes;
    public static boolean draw_graph_edges;

    public GraphRenderer(App $p5) {
        super($p5);
    }

    public void draw() {
        if (update_graph) {
            totalArea = 0;
            for (Edge e : edges) {
                e.update();
            }
            for (Node n : nodes) {
                n.update();
                totalArea += n.getSize();
            }
        }
        if (draw_graph) {
            for (Node n : nodes) {
                int col = (nodes.size() / 2 + (360 / (nodes.size()) * n.getId()));
                if (draw_graph_nodes) drawNode(n);
                if (draw_graph_info) drawInfo(n, col);
                if (draw_graph_list) drawDatablock(n, Info.OUTLINER_X, Info.OUTLINER_Y + (n.id * 14), col);
            }
            if (draw_graph_edges) {
                for (Edge e : edges) drawEdge(e);
            }
        }
    }

    void drawNode(Node n) {

        if (n.rollover(p5.mousePos())) {
            p5.fill(0x881d1d1d);
            p5.stroke(ACTIVE);
            p5.circle(n.particle2D, n.getRadius());
        }

        if (getActiveNode() == n) {
            drawWire(n, Info.OUTLINER_X, Info.OUTLINER_Y + (n.id * 14));
            if (App.debugging) {
                drawBars(n, p5.width / 2, p5.height - 140);
            }
            p5.stroke(ACTIVE);
        } else if (selectedNodes.contains(n)) {
            p5.stroke(SELECTED);
        } else {
            p5.stroke(STANDARD);
        }
        p5.noFill();
        p5.circle(n.particle2D, n.getRadius());
    }

    void drawBars(Node n, float x, float y) {
        float str = n.ball.strength();
        float r = n.getRadius();
        float h = 8;  //bar height

        p5.style(0, 0xff888888);
        p5.textAlign(PApplet.RIGHT);

        p5.text("circ rad: " + StringUtil.DF2.format(r) + " m", x, y += 20);
        p5.rect(x + 10, y - 7, r, h);
        p5.text("node size: " + StringUtil.DF2.format(n.size) + " sq.m", x, y += 20);
        p5.rect(x + 10, y - 7, n.size, h);
        p5.text("meta str: " + StringUtil.DF2.format(str) + " / " + Ball.MAX_STRENGTH, x, y += 20);
        p5.rect(x + 10, y - 7, str, h);
    }

    public void drawWire(Node n, float _x, float _y) {
        _y += 7;
        float xMid = (_x - n.x);
        float x7 = n.x + xMid * 0.7f;
        float x3 = n.x + xMid * 0.3f;

        p5.noFill();
        p5.stroke(0x66888888);
//				p5.line(n._x, n._y, x3, n._y); p5.line(x3, n._y, x7, _y); p5.line(_x, _y, x7, _y);
        p5.bezier(n.x, n.y, x3, n.y, x7, _y, _x, _y);
    }

    private void drawDatablock(Node n, float _x, float _y, int col) {
        float areaPercentage = (n.size / totalArea) * 100;
        /** Dot */
        p5.fill(0xff333333);
        p5.stroke(col, 100, 100);
        p5.circle(_x - 8, _y + 7, 3);
        /** Bar */
        if (n.id % 2 == 0) {
            p5.fill(0xff2b2b2b);
        } else {
            p5.fill(0xff333333);
        }
        p5.noStroke();
        p5.rect(_x, _y + 1, 200, 12);
        p5.stroke(0xff666666);
        p5.line(_x + 120, _y + 1, _x + 120, _y + 12);
        /** Text */
        p5.fill(0xffaeaeae);
        p5.textAlign(PApplet.LEFT);
        p5.text(n.name, _x, _y + 10);
        p5.text(StringUtil.DF1.format(n.size) + "sq.m", _x + 124, _y + 10);
        p5.textAlign(PApplet.RIGHT);
        p5.text(StringUtil.DF1.format(areaPercentage) + "%", _x + 190, _y + 10);
        p5.textAlign(PApplet.LEFT);
    }

    private void drawInfo(Node n, int col) {
        p5.fill(col);
        p5.textAlign(PApplet.CENTER);
        p5.text("[" + n.id + "] " + (int) n.size, n.x, n.y - 10);
        p5.textAlign(PApplet.LEFT);
        p5.noFill();
    }

    public void drawEdge(Edge e) {

        Vec2D va = e.getSpring2D().a;
        Vec2D vb = e.getSpring2D().b;

        p5.noFill();
        if (adjacentEdges.contains(e)) {
            p5.line(va, vb, SELECTED);
        } else if (e == getActiveEdge()) {
            p5.line(va, vb, ACTIVE);
        } else {
            p5.line(va, vb, STANDARD);
        }
        p5.noStroke();
    }
}
