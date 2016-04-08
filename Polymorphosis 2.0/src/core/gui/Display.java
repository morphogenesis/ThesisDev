package core.gui;

import core.App;
import core.Info;
import core.Metaball.Metaball;
import core.graph.GraphRenderer;
import core.graph.Physics;
import core.tools.StringUtil;
import processing.core.PApplet;
import processing.core.PConstants;


/**
 * Created on 3/5/14.
 */
public class Display {

    public static float totalArea = 0;
    private final App p5;
    private float fps = 0;

    public Display(App p5) {
        this.p5 = p5;
    }

    public void draw() {
        drawStatusBar();

        if (App.debugging) {
            p5.fill(0xffcccccc);
            drawSettings(20, 40);
        }
    }

    void drawStatusBar() {
        float x = 100;
        float y = p5.height - 20;
        String mode = "";
        float y2 = p5.height + 14;
        if (p5.frameCount % 60 == 0) {
            fps = p5.frameRate;
        }
        if (Controllers.isEditMode) {
            mode = "EDIT MODE";
        }
        if (Keyboard.isShiftDown) {
            p5.text("SHIFT", x + 200, y);
        }
        p5.text("Java " + PApplet.javaVersionName + PApplet.javaVersion, x + 300, y);

        p5.fill(0xff222222);
        p5.rect(0, y, p5.width, 23);
        p5.fill(0xffffffff);
        p5.noStroke();

        p5.textAlign(PConstants.LEFT);
        p5.text("FPS: " + (int) fps, 20, y2);
        p5.text(mode, x, y2);
        p5.text("file[" + Info.filepath + "]", x * 4, y2);
        p5.fill(0x88ffffff);
        p5.text("tmp[" + Info.tmpFilepath + "]", x * 6, y2);
    }

    void drawSettings(float x, float y) {
        float xx = x + 100;

        for (String s : App.PHYSICS.toStringArray()) {
            p5.text(s, x, y += 12);
        }
        y += 20;
        for (String s : App.METABALL.toStringArray()) {
            p5.text(s, x, y += 12);
        }
        y += 20;
        for (String s : App.VORONOI.toStringArray()) {
            p5.text(s, x, y += 12);
        }
        y += 12;
        p5.text("Area Total", x, y += 12);
        p5.text("Nodes : ", x, y += 12);
        p5.text(GraphRenderer.totalNodeArea() + " sq.m", xx, y);
        p5.text("Cells : ", x, y += 12);
        p5.text(Info.totalRegionArea() + " sq.m", xx, y);
        p5.text("Diff : ", x, y += 12);
        p5.text(Info.totalAreaVariance() + " sq.m", xx, y);
        y += 12;
        p5.text("Metaball", x, y += 12);
        p5.text("Polys: ", x, y += 12);
        p5.text(Metaball.getPolygons().size(), xx, y);
        p5.text("Verts: ", x, y += 12);
        p5.text(Metaball.getPoints().size(), xx, y);
        y += 12;
        p5.text("Iterations:    ", x, y += 12);
        p5.text(StringUtil.DF3.format(Physics.physics.getNumIterations()), xx, y);
        p5.text("Total Springs", x, y += 12);
        p5.text(String.valueOf(Physics.physics.springs.size()), xx, y);
        p5.text("Total Particles", x, y += 12);
        p5.text(String.valueOf(Physics.physics.particles.size()), xx, y);
        p5.text("Total Behaviors", x, y += 12);
        p5.text(String.valueOf(Physics.physics.behaviors.size()), xx, y);
        y += 12;
        p5.text("Meta Threshold", x, y += 12);
        p5.text((App.METABALL.meta_threshold) * 100, xx, y);
        p5.text("Meta Viscosity", x, y += 12);
        p5.text(String.valueOf(App.METABALL.meta_viscosity), xx, y);
        p5.text("Meta Sites", x, y += 12);
        p5.text(App.VORONOI.getNumMetaSites(), xx, y);
    }
}
    /*	if (GraphEditor.update_graph) {
			Display.totalArea = 0;
			for (Edge e : GraphEditor.edges) {e.update();}
			for (Node n : GraphEditor.nodes) { n.update(); Display.totalArea += n.getSize();}
		}
		if (GraphEditor.draw_graph) {
			for (Node n : GraphEditor.nodes) {
				int col = (GraphEditor.nodes.size() / 2 + (360 / (GraphEditor.nodes.size()) * n.getId()));
				if (GraphEditor.draw_graph_nodes) drawNode(n);
				if (GraphEditor.draw_graph_info) drawInfo(n, col);
				if (GraphEditor.draw_graph_list) drawDatablock(n, Info.OUTLINER_X, Info.OUTLINER_Y + (n.id * 14), col);
			}
			if (GraphEditor.draw_graph_edges) { for (Edge e : GraphEditor.edges) drawEdge(e); }
		}*/
//		p5.text("Global Drag", App.DF3.format(Physics.physics.getDrag()));
//		p5.text("Behavior Scale", App.DF3.format(App.PHYSICS.phys_bhavior_str));
//		p5.text("Particle Scale", App.DF3.format(App.PHYSICS.phys_vec_scale));
//		p5.text("Spring Scale ", App.DF3.format(App.PHYSICS.phys_spr_scale));
/*
	void drawNode(Node n) {

		if (n.rollover(p5.mousePos())) {
			p5.fill(0x881d1d1d);
			p5.stroke(ACTIVE); p5.circle(n.particle2D, n.getRadius());
		}

		if (GraphEditor.getActiveNode() == n) {
			drawWire(n, Info.OUTLINER_X, Info.OUTLINER_Y + (n.id * 14));
			if (App.debugging) { drawBars(n, p5.width / 2, p5.height - 140); }
			p5.stroke(ACTIVE);
		}
		else if (GraphEditor.selectedNodes.contains(n)) { p5.stroke(SELECTED); }
		else { p5.stroke(STANDARD); }
		p5.noFill();
		p5.circle(n.particle2D, n.getRadius());
	}*/
	/*void drawBars(Node n, float x, float y) {
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
	}*/
	/*public void drawWire(Node n, float _x, float _y) {
		_y += 7;
		float xMid = (_x - n.x);
		float x7 = n.x + xMid * 0.7f;
		float x3 = n.x + xMid * 0.3f;

		p5.noFill();
		p5.stroke(0x66888888);
//				p5.line(n._x, n._y, x3, n._y); p5.line(x3, n._y, x7, _y); p5.line(_x, _y, x7, _y);
		p5.bezier(n.x, n.y, x3, n.y, x7, _y, _x, _y);
	}*/
/*	private void drawDatablock(Node n, float _x, float _y, int col) {
		float areaPercentage = (n.size / totalArea) * 100;
		*//**
 * Dot  Bar  Text
 *//*
		p5.fill(0xff333333);
		p5.stroke(col, 100, 100);
		p5.circle(_x - 8, _y + 7, 3);
		*//** Bar *//*
		if (n.id % 2 == 0) { p5.fill(0xff2b2b2b); }
		else { p5.fill(0xff333333); }
		p5.noStroke();
		p5.rect(_x, _y + 1, 200, 12);
		p5.stroke(0xff666666);
		p5.line(_x + 120, _y + 1, _x + 120, _y + 12);
		*//** Text *//*
		p5.fill(0xffaeaeae);
		p5.textAlign(PApplet.LEFT);
		p5.text(n.name, _x, _y + 10);
		p5.text(StringUtil.DF1.format(n.size) + "sq.m", _x + 124, _y + 10);
		p5.textAlign(PApplet.RIGHT);
		p5.text(StringUtil.DF1.format(areaPercentage) + "%", _x + 190, _y + 10);
		p5.textAlign(PApplet.LEFT);
	}*/
/*	private void drawInfo(Node n, int col) {
		p5.fill(col);
		p5.textAlign(PApplet.CENTER);
		p5.text("[" + n.id + "] " + (int) n.size, n.x, n.y - 10);
		p5.textAlign(PApplet.LEFT);
		p5.noFill();
	}*/
/*	public void drawEdge(Edge e) {

		Vec2D va = e.getSpring2D().a;
		Vec2D vb = e.getSpring2D().b;

		p5.noFill();
		if (GraphEditor.adjacentEdges.contains(e)) {p5.line(va, vb, SELECTED); }
		else if (e == GraphEditor.getActiveEdge()) { p5.line(va, vb, ACTIVE);}
		else {p5.line(va, vb, STANDARD);}
		p5.noStroke();
	}*/
