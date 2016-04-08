package core.gui;

import controlP5.*;
import core.App;
import core.Info;
import core.graph.GraphRenderer;
import core.graph.map.Node;
import core.tools.Color;
import org.philhosoft.p8g.svg.P8gGraphicsSVG;
import processing.core.PVector;

import java.io.File;

import static core.App.*;

public class Controllers {
    public static ControlP5 CP5 = null;
    public static boolean isEditMode = true;
    //	public static  Slider    meta_visc;
    private static Knob objSize;
    private static Knob capacitySlider;
    private static Textfield objName;
    private static Knob colorSlider;
    private final App p5;
    public Println console;
    public Textarea myTextarea;

    public Controllers(App p5) {
        this.p5 = p5;
    }

    public static void hideController(String controllerName) {
        Controller a = CP5.getController(controllerName);
        float x = a.getPosition().x;
        float y = a.getPosition().y;
        if ((x > 100 - a.getWidth())) a.setPosition(x - 10, y);
    }

    public static void showController(String controllerName) {
        Controller a = CP5.getController(controllerName);
        float x = a.getPosition().x;
        float y = a.getPosition().y;
        if (x < 0) a.setPosition(x + 10, y);
    }

    public void setup() {
        setupInfoPanel();
        setupSettingsPanel();
        setupViewPanel();
//		setupPropertyPanel();
//		outlinerEditor();
        ControllerStyle.init();
        addMouseWheelListener();
        CP5.update();
    }

    public void init() {
        CP5 = new ControlP5(p5);
        CP5.enableShortcuts();
        CP5.setAutoDraw(false);
        CP5.setFont(App.pfont8, 8);
        CP5.setAutoSpacing(4, 4);
        CP5.setColorBackground(Color.CP5_BG);
        CP5.setColorForeground(Color.CP5_FG);
        CP5.setColorActive(Color.CP5_ACT);
        CP5.setColorCaptionLabel(Color.CP5_CAP).setColorValueLabel(Color.CP5_VAL);
        Controller.autoHeight = 14;
        Controller.autoSpacing = new PVector(0, 3, 0);
        Button.autoWidth = 90;
        Button.autoSpacing = new PVector(5, 5, 0);
        Toggle.autoHeight = 10;
        Toggle.autoWidth = 10;
        Numberbox.autoWidth = 200;
        myTextarea = CP5.addTextarea("txt").setFont(App.pfont8).setPosition(220, 40).setSize(320, 1000).setLineHeight(14).setColor(0xffcccccc).disableColorBackground().setColorForeground(0xffffffff);
        console = CP5.addConsole(myTextarea);
    }

    public void draw() {
        CP5.draw();
//		console.play();
    }

    public void controlEvent(App app, ControlEvent theEvent) {
        if (!theEvent.isGroup()) {
            float theValue = theEvent.getController().getValue();
            //System.out.println(theEvent.getController().getName() + "=>" + theValue);
            switch (theEvent.getController().getName()) {
                case "file_quit":
                    app.exit();
                    break;
                case "file_new":
                    App.GRAPH.clearGraph();
                    App.PHYSICS.clear();
                    App.METABALL.clear();
                    App.VORONOI.clear();
                    break;
                case "file_open":
                    GraphRenderer.openXML(Info.file);
                    break;
                case "file_open_temp":
                    GraphRenderer.openXML(Info.tmp);
                    break;
                case "file_save":
                    console.pause();
                    File file = new File(Info.filepath);
                    GraphRenderer.saveXML(Info.file);
                    break;
//				case "file_temp": File tmp = new File(Info.tmpFilepath); GraphBuilder.saveXML(tmp); break;
                case "file_save_temp":
                    File tmp = new File(Info.filepath2);
                    GraphRenderer.saveXML(Info.tmp);
                    break;
                case "file_print":
                    app.beginRecord(P8gGraphicsSVG.SVG, Info.exportpath);
                    App.RECORDING = true;
                    break;
            }
        }
    }

    void toggleObjProperties() {
        if (GraphRenderer.hasActiveNode()) {
            objSize.setValue(GraphRenderer.getActiveNode().getSize());
            colorSlider.setValue(GraphRenderer.getActiveNode().getType());
            capacitySlider.setValue(GraphRenderer.getActiveNode().getOccupancy());
            objName.setValue(GraphRenderer.getActiveNode().getName());
            objSize.show();
            colorSlider.show();
            capacitySlider.show();
            objName.show();
            objName.setPosition(Info.OUTLINER_X, Info.OUTLINER_Y + GraphRenderer.getActiveNode().getId() * 14);
        } else {
            objSize.hide();
            colorSlider.hide();
            capacitySlider.hide();
            objName.hide();
        }
    }

    private void addMouseWheelListener() {
        p5.frame.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                CP5.setMouseWheelRotation(e.getWheelRotation());
            }
        });
    }

    private void setupViewPanel() {
        Group viewPanel = CP5.addGroup("viewPanel").setBarHeight(24).setWidth(120).setPosition(221, 23).enableCollapse();
        Group vorGroup = CP5.addGroup("voronoi_settings").setBackgroundHeight(getGroupHeight(-1, 10)).setWidth(120);
        Group groupPhysics = CP5.addGroup("physics_display").setBackgroundHeight(getGroupHeight(-1, 3));
        Group graphGroup = CP5.addGroup("graph_display").setBackgroundHeight(getGroupHeight(-1, 5));
        Accordion accordionView = CP5.addAccordion("accordionView");
        accordionView.setPosition(0, -1).setWidth(120).setCollapseMode(Accordion.MULTI);
        accordionView.addItem(groupPhysics);
        accordionView.addItem(vorGroup);
        accordionView.addItem(graphGroup);
        accordionView.open().setGroup(viewPanel);
        viewPanel.close();

        CP5.begin(10, 12);
        CP5.addToggle("update_voronoi").plugTo(VORONOI).setGroup(vorGroup).setValue(true).setCaptionLabel("update_voronoi").linebreak();
        CP5.addToggle("draw_voronoi").plugTo(VORONOI).setGroup(vorGroup).setValue(true).setCaptionLabel("draw_voronoi").linebreak();
        CP5.addToggle("draw_vor_poly").plugTo(VORONOI).setGroup(vorGroup).setValue(true).setCaptionLabel("vor_polygons").linebreak();
        CP5.addToggle("draw_vor_bez").plugTo(VORONOI).setGroup(vorGroup).setValue(false).setCaptionLabel("vor_bezier").linebreak();
        CP5.addToggle("draw_vor_vec").plugTo(VORONOI).setGroup(vorGroup).setValue(false).setCaptionLabel("vor_vertices").linebreak();
        CP5.addToggle("draw_vor_info").plugTo(VORONOI).setGroup(vorGroup).setValue(false).setCaptionLabel("vor_info").linebreak();
        CP5.addToggle("draw_vor_metaball").plugTo(VORONOI).setGroup(vorGroup).setValue(true).setCaptionLabel("vor_metaball").linebreak();
        CP5.addToggle("draw_vor_offset").plugTo(VORONOI).setGroup(vorGroup).setValue(true).setCaptionLabel("vor_offset").linebreak();
        CP5.addToggle("draw_metaball").plugTo(METABALL).setGroup(vorGroup).setValue(true).setCaptionLabel("draw_metaball").linebreak();
        CP5.addToggle("draw_meta_info").plugTo(METABALL).setGroup(vorGroup).setValue(true).setCaptionLabel("meta_info").linebreak();
        CP5.end();

        CP5.begin(10, 12);
        CP5.addToggle("update_physics").plugTo(PHYSICS).setGroup(groupPhysics).setValue(true).setCaptionLabel("update_physics").linebreak();
        CP5.addToggle("draw_physics").plugTo(PHYSICS).setGroup(groupPhysics).setValue(true).setCaptionLabel("draw_physics").linebreak();
        CP5.addToggle("draw_phys_info").plugTo(PHYSICS).setGroup(groupPhysics).setValue(false).setCaptionLabel("draw_phys_info").linebreak();
        CP5.end();

        CP5.begin(10, 12);
        CP5.addToggle("update_graph").plugTo(GRAPH).setValue(true).setGroup(graphGroup).linebreak();
        CP5.addToggle("draw_graph").plugTo(GRAPH).setValue(true).setGroup(graphGroup).linebreak();
        CP5.addToggle("draw_graph_list").plugTo(GRAPH).setValue(true).setGroup(graphGroup).linebreak();
        CP5.addToggle("draw_graph_nodes").plugTo(GRAPH).setValue(true).setGroup(graphGroup).linebreak();
        CP5.addToggle("draw_graph_edges").plugTo(GRAPH).setValue(true).setGroup(graphGroup).linebreak();
        CP5.end();
    }

    private void setupSettingsPanel() {
        Group settingsPanel = CP5.addGroup("settingsPanel").setBackgroundHeight(p5.height).setBarHeight(24).setWidth(220).setPosition(0, 23).enableCollapse();
        Group physGroup = CP5.addGroup("physics_settings").setBackgroundHeight(getGroupHeight(9, -1));
        Group metaGroup = CP5.addGroup("metaball_settings").setBackgroundHeight(getGroupHeight(7, -1));
        Accordion leftAccordion = CP5.addAccordion("leftAccordion");
        leftAccordion.setPosition(0, 0).setWidth(220).setCollapseMode(Accordion.MULTI).setColorBackground(0xff1d1d1d);
        leftAccordion.addItem(physGroup);
        leftAccordion.addItem(metaGroup);
        leftAccordion.open().setGroup(settingsPanel);
        settingsPanel.close();

        CP5.begin(10, 12);
        CP5.addSlider("WORLD_SCALE").plugTo(PHYSICS).setGroup(physGroup).setValue(10).setRange(1, 20).setDecimalPrecision(0).linebreak();
        CP5.addSlider("phys_drag").plugTo(PHYSICS).setGroup(physGroup).setValue(0.3f).setRange(.1f, 1).setDecimalPrecision(2).linebreak();
        CP5.addSlider("phys_vec_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(1).setRange(.5f, 2).setDecimalPrecision(1).linebreak();
        CP5.addSlider("phys_spr_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(1).setRange(.9f, 1.1f).setDecimalPrecision(2).linebreak();
        CP5.addSlider("phys_spr_strength").plugTo(PHYSICS).setGroup(physGroup).setValue(0.01f).setRange(.001f, .009f).setDecimalPrecision(3).linebreak();
        CP5.addSlider("phys_vec_wght").plugTo(PHYSICS).setGroup(physGroup).setValue(.1f).setRange(.01f, 3).setDecimalPrecision(1).linebreak();
        CP5.addSlider("phys_bhavior_scale").plugTo(PHYSICS).setGroup(physGroup).setValue(2).setRange(1, 3).setDecimalPrecision(1).linebreak();
        CP5.addSlider("phys_bhavior_str").plugTo(PHYSICS).setGroup(physGroup).setValue(-2f).setRange(-1f, -5).setDecimalPrecision(2).linebreak();
        CP5.addSlider("phys_mindist_str").plugTo(PHYSICS).setGroup(physGroup).setValue(0.01f).setRange(.001f, .05f).setDecimalPrecision(2).linebreak();
        CP5.end();

        CP5.begin(10, 12);
//		meta_visc = CP5.addSlider("meta_viscosity").plugTo(METABALL).setGroup(metaGroup).setRange(1, 3).setValue(2).setDecimalPrecision(1).linebreak();
        CP5.addSlider("meta_viscosity").plugTo(METABALL).setGroup(metaGroup).setRange(1, 3).setValue(2).setDecimalPrecision(1).linebreak();
        CP5.addSlider("meta_threshold").plugTo(METABALL).setGroup(metaGroup).setRange(0.0001f, 0.0009f).setValue(0.0006f).setDecimalPrecision(4).linebreak();
        CP5.addSlider("meta_resolution").plugTo(METABALL).setGroup(metaGroup).setValue(20).setRange(5, 100).setDecimalPrecision(0).linebreak();
        CP5.addSlider("meta_maxSteps").plugTo(METABALL).setGroup(metaGroup).setValue(400).setRange(0, 800).setDecimalPrecision(1).linebreak();
        CP5.addSlider("meta_maxStrength").plugTo(METABALL).setGroup(metaGroup).setValue(200).setRange(3, 800).setDecimalPrecision(0).linebreak();
        CP5.addSlider("vor_offset").plugTo(VORONOI).setGroup(metaGroup).setValue(-2).setRange(-10, 10).setDecimalPrecision(0).linebreak();
        CP5.addSlider("vor_meta_res").plugTo(VORONOI).setGroup(metaGroup).setValue(10).setRange(5, 200).setDecimalPrecision(0).linebreak();
        CP5.end();
    }

    private int getGroupHeight(int numSliders, int numToggles) {
        return ((numSliders) * 22) + ((numToggles) * 15) + 24;
    }

    private void setupInfoPanel() {
        CP5.begin(342, 0);
        CP5.addButton("file_open").setCaptionLabel("Open XML");
        CP5.addButton("file_open_temp").setCaptionLabel("Open TMP");
        CP5.addButton("file_save").setCaptionLabel("Save XML");
        CP5.addButton("file_save_temp").setCaptionLabel("Save TMP");
        CP5.addButton("file_print").setCaptionLabel("Print SVG");

        CP5.addToggle("debugging").setSize(22, 22);
        CP5.addButton("file_quit").setCaptionLabel("Quit");
        CP5.addButton("file_new").setCaptionLabel("New");
        CP5.end();
    }

    class nameTextfieldListener implements ControlListener {
        public void controlEvent(ControlEvent e) {
            String name = e.getController().getStringValue();
            if (GraphRenderer.hasActiveNode()) GraphRenderer.getActiveNode().setName(name);
        }
    }

    class colorSliderListener implements ControlListener {
        public void controlEvent(ControlEvent e) {
            float color = e.getController().getValue();
            if (CP5.isMouseOver()) {
                if (GraphRenderer.hasActiveNode()) GraphRenderer.getActiveNode().setType((int) color);
                for (Node n : GraphRenderer.selectedNodes) {
                    n.setType((int) color);
                }
            }
        }
    }

    class capacitySliderListener implements ControlListener {
        public void controlEvent(ControlEvent e) {
//		float	capacity = e.getController().getValue(); if (App.CP5.isMouseOver()) { for (Node g : Editor.selectedNodes) {g.setOccupancy((int) capacity);} }
        }
    }

    class sizeSliderListener implements ControlListener {
        public void controlEvent(ControlEvent e) {
            float size = e.getController().getValue();
            if (CP5.isMouseOver()) {
                App.GRAPH.scaleNode(size);
            }
        }
    }
}

	/*private void setupPropertyPanel() {
        Accordion rightAccordion = CP5.addAccordion("rightAccordion");
		rightAccordion.setPosition(p5.width - 340, 0).setWidth(220);
		Group toolGroup = CP5.addGroup("tool_group").setBackgroundHeight(140).setPosition(0, 0);
		CP5.begin(0, 0);
		objSize = CP5.addKnob("setSize").setGroup(toolGroup).setCaptionLabel("Size").addListener(new sizeSliderListener()).setRange(0, 500).setValue(50).setPosition(10, 30).setDecimalPrecision(1).hide();
		colorSlider = CP5.addKnob("setType").setGroup(toolGroup).setCaptionLabel("Type").addListener(new colorSliderListener()).setPosition(80, 30).setRange(0, 6).setValue(3).setDecimalPrecision(0).hide();
		capacitySlider = CP5.addKnob("setCapacity").setGroup(toolGroup).setCaptionLabel("Capacity").addListener(new capacitySliderListener()).setPosition(150, 30).setRange(1, 200).setValue(1).setDecimalPrecision(0).hide();
		CP5.end();
		rightAccordion.addItem(toolGroup);
	}
	private void outlinerEditor() {
		objName = CP5.addTextfield("setName").setCaptionLabel("Name").addListener(new nameTextfieldListener()).setPosition(0, 0).setStringValue("untitled").hide();
	}*/
