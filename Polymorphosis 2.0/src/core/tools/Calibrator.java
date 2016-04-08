package core.tools;

import core.App;
import core.Info;
import core.VoronoiSys;
import core.graph.GraphBuilder;
import core.graph.GraphRenderer;
import core.graph.map.Node;
import toxi.geom.Polygon2D;


public class Calibrator {

    App p5;
    float WS;

    public Calibrator(App p5) {
        this.p5 = p5;
    }

    public void calibrateThreshold() {
        if (Math.abs(GraphRenderer.totalNodeArea() - Info.totalRegionArea()) > 2) {
            if (GraphRenderer.totalNodeArea() < Info.totalRegionArea()) {
//				Controllers.meta_visc.setValue(Controllers.meta_visc.getValue() + 0.01f);
                System.out.print("+");
            }
            if (GraphRenderer.totalNodeArea() > Info.totalRegionArea()) {
//				Controllers.meta_visc.setValue(Controllers.meta_visc.getValue() - 0.01f);
                System.out.print("-");
            }
        }
    }

    public void calibrateMetaball() {
        WS = App.WORLD_SCALE * App.WORLD_SCALE;
        for (Polygon2D p : VoronoiSys.getInteriorRegions()) {

            for (Node n : GraphBuilder.nodes) {
                if (p.containsPoint(n.particle2D)) {
                    float pa = p.getArea() / WS;
                    float na = n.getSize();
                    float mbs = n.metaballScale;

                    if (Math.abs(pa - na) > 2) {
                        if (pa > na) {
                            n.metaballScale = mbs - 1;
                        }
                        if (pa < na) {
                            n.metaballScale = mbs + 1;
                        }
                    }
                }
            }
        }
    }

    public void calibrateActiveMetaball() {
        if (GraphRenderer.getActiveNode() != null) {
            WS = App.WORLD_SCALE * App.WORLD_SCALE;
            for (Polygon2D p : VoronoiSys.getInteriorRegions()) {
                if (p.containsPoint(GraphRenderer.getActiveNode().particle2D)) {
                    float pa = p.getArea() / WS;
                    float na = GraphRenderer.getActiveNode().getSize();
                    float mbs = GraphRenderer.getActiveNode().metaballScale;

                    if (Math.abs(pa - na) > 2) {
                        if (pa > na) {
                            GraphRenderer.getActiveNode().metaballScale = mbs - 1;
                        }
                        if (pa < na) {
                            GraphRenderer.getActiveNode().metaballScale = mbs + 1;
                        }
                    }
                }
            }
        }
    }
}

