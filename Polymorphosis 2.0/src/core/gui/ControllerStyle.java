package core.gui;

import controlP5.*;
import core.tools.Color;

import static core.App.pfont10;
import static core.App.pfont8;


/**
 * Created on 3/11/14.
 */
public class ControllerStyle {
    public ControllerStyle() {
    }

    public static void init() {
        for (Knob k : Controllers.CP5.getAll(Knob.class)) styleKnobs(k);
        for (Toggle t : Controllers.CP5.getAll(Toggle.class)) styleToggles(t);
        for (Button b : Controllers.CP5.getAll(Button.class)) styleButtons(b);
        for (Slider s : Controllers.CP5.getAll(Slider.class)) styleSliders(s);
        for (Numberbox n : Controllers.CP5.getAll(Numberbox.class)) styleNumberbox(n);
        for (Textfield t : Controllers.CP5.getAll(Textfield.class)) styleTextfields(t);
        for (Textlabel t : Controllers.CP5.getAll(Textlabel.class)) styleTextlabel(t);
        for (Group g : Controllers.CP5.getAll(Group.class)) styleGroup(g);
        for (Accordion a : Controllers.CP5.getAll(Accordion.class)) a.setMinItemHeight(48).updateItems();
    }

    protected static void styleTextlabel(Textlabel textlabel) {
        textlabel.setSize(115, 26);
        textlabel.setLineHeight(22);
        textlabel.setFont(pfont8);
    }

    protected static void styleTextfields(Textfield textfield) {
        textfield.setSize(120, 12);
        textfield.setAutoClear(false);
        textfield.getCaptionLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).getStyle().setPaddingTop(4);
        textfield.setColor(0xff000000);
        textfield.setColorBackground(0xffffffff);
        textfield.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER);
        textfield.setColorForeground(0xffffffff);
//			textfield.getCaptionLabel().hide();
    }

    protected static void styleSliders(Slider slider) {
        slider.setSize(200, 14);
        slider.showTickMarks(false);
        slider.setSliderMode(Slider.FLEXIBLE);
        slider.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(2);
        slider.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER);
//			slider.getCaptionLabel().disableColorBackground();
    }

    protected static void styleNumberbox(Numberbox numberbox) {
        numberbox.setSize(200, 14);
        numberbox.setMultiplier(0.001f);
        numberbox.setDirection(ControlP5.HORIZONTAL);
        numberbox.getValueLabel().align(ControlP5.LEFT, ControlP5.CENTER).getStyle().setPaddingLeft(2);
        numberbox.getCaptionLabel().align(ControlP5.RIGHT, ControlP5.CENTER);
//			numberbox.getCaptionLabel().disableColorBackground();
    }

    protected static void styleButtons(Button button) {
        button.setSize(90, 22);
        button.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
        button.setColorCaptionLabel(0xffffffff);
    }

    protected static void styleKnobs(Knob knob) {
        knob.setRadius(30);
        knob.setDragDirection(Knob.HORIZONTAL);
    }

    protected static void styleToggles(Toggle toggle) {
        toggle.setMode(ControlP5.DEFAULT);
        toggle.setColorActive(Color.CP5_FG);
        toggle.setColorForeground(0xff444444);
        toggle.setColorBackground(0xff666666);
        toggle.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setFont(pfont8).getStyle().setPaddingLeft(4);
    }

    protected static void styleGroup(Group group) {
        group.setBackgroundColor(0xff1d1d1d);
        group.showBar();
        group.setBarHeight(24);
        group.setColorLabel(0xffcccccc);
        group.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
        group.getCaptionLabel().setFont(pfont10);
    }
}
