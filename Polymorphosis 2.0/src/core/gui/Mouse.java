package core.gui;

import core.graph.GraphRenderer;
import core.graph.map.Node;
import processing.event.MouseEvent;
import toxi.geom.Vec2D;

import java.awt.event.KeyEvent;


/**
 * Created on 3/9/14.
 */
public final class Mouse {

    static public final int PRESS = 1;
    static public final int RELEASE = 2;
    static public final int CLICK = 3;
    static public final int DRAG = 4;
    static public final int MOVE = 5;
    static public final int ENTER = 6;
    static public final int EXIT = 7;
    static public final int WHEEL = 8;
    static final int LEFT = KeyEvent.VK_LEFT;
    static final int RIGHT = KeyEvent.VK_RIGHT;

    public Mouse() {
    }

    public void mouseEvent(MouseEvent event) {
        switch (event.getAction()) {
            case PRESS:
                System.out.println("Mouse Press");
                break;
            case RELEASE:
                System.out.println("Mouse RELEASE");
                break;
            case CLICK:
                System.out.println("Mouse CLICK");
                break;
            case DRAG:
                break;
            case MOVE:
                break;
            case WHEEL:
                System.out.println("Mouse WHEEL");
                break;
        }
    }

    public void mousePressed(Vec2D pos, int button) {
        if (button == RIGHT) {
            GraphRenderer.selectNodeNearPosition(pos);
        }

        if (button == LEFT) {
            GraphRenderer.deselectNode();
        }
    }

    public void mouseReleased(Vec2D pos, int button) {
        if (button == RIGHT) {
            GraphRenderer.releaseNode();
        }
    }

    public void mouseDragged(Vec2D pos, int button) {
        if (button == RIGHT) {
            if (Controllers.isEditMode) {
                if (GraphRenderer.hasActiveNode()) {
                    GraphRenderer.moveNode(pos);
                }
            }
        }
    }

    public void mouseMoved(Vec2D pos) {
    }

    public void mouseWheel(float event) {
        if (GraphRenderer.hasActiveNode()) {
            float size = GraphRenderer.getActiveNode().getSize();
            float scale = 0.1f;
            if (size >= 3) {
                scale = 1;
            }
            if (size >= 20) {
                scale = 5;
            }
            if (size >= 50) {
                scale = 12;
            }
            if (size >= 100) {
                scale = 25;
            }
            if (event > 0) {
                GraphRenderer.getActiveNode().setSize(size - scale);
            } else if (event < 0) {
                GraphRenderer.getActiveNode().setSize(size + scale);
            }
            if (GraphRenderer.getActiveNode().getSize() <= 1) {
                GraphRenderer.getActiveNode().setSize(2);
            }
        }
        for (Node n : GraphRenderer.selectedNodes) {
            n.setSize(GraphRenderer.getActiveNode().getSize());
        }
    }
}
