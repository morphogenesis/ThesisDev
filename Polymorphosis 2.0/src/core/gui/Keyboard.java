package core.gui;

import core.App;
import core.graph.GraphRenderer;

import java.awt.event.KeyEvent;


/**
 * Created on 3/8/14.
 */
public final class Keyboard {
    static final char BACKSPACE = 8;
    static final char TAB = 9;
    static final char ENTER = 10;
    static final char RETURN = 13;
    static final char ESC = 27;
    static final char DELETE = 127;
    // i.e. if ((key == CODED) && (keyCode == UP))
    static final int CODED = 0xffff;
    static final int UP = KeyEvent.VK_UP;
    static final int DOWN = KeyEvent.VK_DOWN;
    static final int LEFT = KeyEvent.VK_LEFT;
    static final int RIGHT = KeyEvent.VK_RIGHT;
    static final int ALT = KeyEvent.VK_ALT;
    static final int CONTROL = KeyEvent.VK_CONTROL;
    static final int SHIFT = KeyEvent.VK_SHIFT;
    static final int ADD = KeyEvent.VK_ADD;
    static final int DIVIDE = KeyEvent.VK_DIVIDE;
    public static boolean isShiftDown;
    public static boolean isCtrlDown;
    static int lastNum = 1;
    static float cachedSize = 50;
    static String cachedName = "untitled";
    private final App p5;

    public Keyboard(App p5) {
        this.p5 = p5;
    }

    public void keyPressed(char key) {
        if (key == CODED) {
            switch (p5.keyCode) {
                case SHIFT:
                    isShiftDown = true;
                    break;
                case CONTROL:
                    isCtrlDown = true;
                    break;
                case UP:
                    GraphRenderer.addDuplicate(lastNum, false);
                    break;
                case DOWN:
                    GraphRenderer.addDuplicate(lastNum, true);
                    break;
            }
        } else System.out.print(":" + key);
        switch (key) {
            case TAB:
                Controllers.isEditMode = !Controllers.isEditMode;
                break;
            case '1':
                lastNum = 1;
                break;
            case '2':
                lastNum = 2;
                break;
            case '3':
                lastNum = 3;
                break;
            case '4':
                lastNum = 4;
                break;
            case '5':
                lastNum = 5;
                break;
            case '6':
                lastNum = 6;
                break;
            case '7':
                lastNum = 7;
                break;
            case '8':
                lastNum = 8;
                break;
            case '9':
                lastNum = 9;
                break;
            case 'a':
                App.GRAPH.makeNode(cachedName, cachedSize, p5.mousePos());
                break;
            case 'f':
                App.GRAPH.makeEdge();
                break;
            case 'l':
                GraphRenderer.lockNode();
                break;

            case 'x':
                if (isCtrlDown) {
                    App.GRAPH.delete("nodes");
                } else {
                    App.GRAPH.delete("node");
                }
                break;
            case 'v':
                if (isCtrlDown) {
                    App.GRAPH.delete("edges");
                } else {
                    App.GRAPH.delete("edge");
                }
                break;

            case 'z':
                App.METABALL.clear();
                break;
            case 'j':
                App.PHYSICS.addMinDist();
                break;
            case 't':
                p5.controllers.init();
                p5.controllers.setup();
                break;
            case 'p':
                p5.saveFrame();
                break;
            case 'q':
                System.out.println(p5.millis());
                break;

//				case 'i': calibrator.calibrateActiveMetaball(); break;
//				case 'o': calibrator.calibrateThreshold(); break;
//				case 'p': calibrator.calibrateMetaball(); break;
        }
    }

    public void keyReleased(char key) {
        if (key == CODED) {
            switch (p5.keyCode) {
                case SHIFT:
                    isShiftDown = false;
                    break;
                case CONTROL:
                    isCtrlDown = false;
                    break;
            }
        }
    }
    /*void add_node() {
		App.GRAPH.createNewNode(controllers.objName.getStringValue(), controllers.objSize.getValue(), mousePos()); controllers.toggleObjProperties();
	}*/
}
