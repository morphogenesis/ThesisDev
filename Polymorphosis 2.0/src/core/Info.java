package core;

import core.graph.GraphRenderer;
import core.graph.map.Node;
import toxi.geom.Polygon2D;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class Info {

    public static final String tmpFilepath = "./data/xml/tmp.xml";
    public static final String exportpath = "./data/svg/print-###.svg";
    public static final String fontPath = "./data/fonts/";
    public static final File tmp = new File("./data/xml/tmp.xml");
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1000;
    public static final int OUTLINER_X = 1400;
    public static final int OUTLINER_Y = 50;
    public static final String timestamp = new SimpleDateFormat("yyyy-MM-dd'v'HH").format(new Date());
    public static final String timestampShort = new SimpleDateFormat("MMM-dd hh-mm-a").format(new Date());
    public static final String filename = "thesis_" + timestamp + ".xml";
    public static final File file = new File("./data/xml/" + filename);
    public static final String filepath = "./data/xml/" + filename;
    public static final String filepath2 = "./data/xml/tmp" + filename;

    public static int totalAreaVariance() {
        float totalNodeArea = 0;
        float totalRegionArea = 0;
        float totalDif = 0;

        for (Node n : GraphRenderer.nodes) {
            totalNodeArea += n.getSize();
            for (Polygon2D p : VoronoiSys.getInteriorRegions()) {
                if (p.containsPoint(n.particle2D)) {
                    totalRegionArea += p.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE);
                }
            }
        }
        return (int) Math.abs(totalNodeArea - totalRegionArea);
    }

    public static int totalRegionArea() {
        float totalRegionArea = 0;
        for (Polygon2D p : VoronoiSys.getInteriorRegions()) {
            totalRegionArea += p.getArea() / (App.WORLD_SCALE * App.WORLD_SCALE);
        }
        return (int) Math.abs(totalRegionArea);
    }
}
