package core;

import controlP5.ControlEvent;
import core.Metaball.Metaball;
import core.graph.GraphRenderer;
import core.graph.Physics;
import core.gui.Controllers;
import core.gui.Display;
import core.gui.Keyboard;
import core.gui.Mouse;
import core.tools.Color;
import core.tools.Vector2D;
import g4p_controls.GTimer;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletSpring2D;


public class App extends PApplet {
    public static boolean RECORDING = false;
    public static boolean PRINTING = false;
    public static PFont pfont8, pfont10, pfont12;
    public static Physics PHYSICS;
    public static GraphRenderer GRAPH;
    public static VoronoiSys VORONOI;
    public static Metaball METABALL;
    public static float WORLD_SCALE = 10;
    public static boolean debugging = true;
    public final Controllers controllers = new Controllers(this);
    //	private final Calibrator  calibrator  = new Calibrator(this);
    private final Display UI = new Display(this);
    private final Keyboard keyboard = new Keyboard(this);
    private final Mouse mouse = new Mouse();
    public float snapDist = 10 * 10;
    GTimer timer = null;
    int rate = 600000;// 10 minutes

    public static void main(String[] args) {
        PApplet.main(new String[]{("core.App")});
    }

    public static void __rebelReload() {
        System.out.println("********************  rebelReload  ********************");
        System.out.println("Current File: " + Info.filepath);
    }


    public void setup() {
        size(1600, 1000);
        smooth(4);
        frame.setResizable(true);
        colorMode(HSB, 360, 100, 100, 100);
        background(Color.BG);
        ellipseMode(RADIUS);
        strokeWeight(1);
        noStroke();
        noFill();

        pfont8 = loadFont(Info.fontPath + "SourceCodePro-Regular-8.vlw");
        pfont10 = loadFont(Info.fontPath + "SourceCodePro-Regular-10.vlw");
        pfont12 = loadFont(Info.fontPath + "SourceCodePro-Regular-12.vlw");
        textAlign(LEFT);
        textFont(pfont10, 10);

        rate = 600000; //10min
        timer = new GTimer(this, this, "printScreen", rate);
        timer.start(10);
        PHYSICS = new Physics(this);
        GRAPH = new GraphRenderer(this);
        METABALL = Metaball.getInstance(this);
        VORONOI = new VoronoiSys(this);
        controllers.init();
        controllers.setup();
    }

    public void draw() {
        background(Color.BG);
        noFill();
        noStroke();
        if (focused) {
            PHYSICS.draw();
            GRAPH.draw();
//			METABALL.draw();
//			VORONOI.update();
//			VORONOI.draw();
        }
        if (RECORDING) {
            RECORDING = false;
            endRecord();
            System.out.println("SVG OUT");
        }
        UI.draw();
        controllers.draw();
        if (frameCount == 60) saveFrame("./data/tif/screen-" + Info.timestampShort + ".tif");
    }

    public void mousePressed() {
        mouse.mousePressed(mousePos(), mouseButton);
    }

    public void mouseReleased() {
        mouse.mouseReleased(mousePos(), mouseButton);
    }//		if (mouseButton == RIGHT) GraphObjectImpl.mouseReleased();

    public void mouseDragged() {
        mouse.mouseDragged(mousePos(), mouseButton);
    }//		if (mouseButton == RIGHT) GraphObjectImpl.mouseDragged(mousePos());

    public void mouseMoved() {
        mouse.mouseMoved(mousePos());
    }//		GraphObjectImpl.mouseMoved(mousePos());

    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        mouse.mouseWheel(e);
    }

    public void keyPressed() {
        keyboard.keyPressed(key);
    }

    public void keyReleased() {
        keyboard.keyReleased(key);
    }

    public Vec2D mousePos() {
        return new Vec2D(mouseX, mouseY);
    }

    public void circle(float x, float y, float r) {
        if (recorder != null) recorder.ellipse(x, y, r, r);
        ellipse(x, y, r, r);
    }

    public void circle(Vector2D v, float r) {
        ellipse(v.x, v.y, r, r);
    }

    public void circle(Vec2D v, float r) {
        ellipse(v.x, v.y, r, r);
    }

    public void circle(Vec2D v, float r, int col) {
        stroke(col);
        circle(v, r);
        ellipse(v.x, v.y, r, r);
    }

    public void circle(Vec2D v, float r, int stroke, int fill) {
        stroke(stroke);
        fill(fill);
        ellipse(v.x, v.y, r, r);
    }

    public void controlEvent(ControlEvent theEvent) {
        controllers.controlEvent(this, theEvent);
    }

    public void line(Vec2D a, Vec2D b, int col) {
        stroke(col);
        line(a.x, a.y, b.x, b.y);
    }

    public void line(VerletSpring2D spring, int col) {
        stroke(col);
        line(spring.a.x, spring.a.y, spring.b.x, spring.b.y);
    }

    public void printScreen(GTimer timer) {
        System.out.println("Generating screenshot..../data/tif/screen-" + nf(frameCount, 4) + ".tif");
        saveFrame("./data/tif/screen-" + Info.timestampShort + ".tif");
    }

    public void style(int stroke, int fill) {
        if (stroke == 0) {
            noStroke();
        } else {
            stroke(stroke);
        }
        if (fill == 0) {
            noFill();
        } else {
            fill(fill);
        }
    }
}
