package core.Metaball;

import core.tools.MathUtil;
import core.tools.Vector2D;


/**
 * Created on 3/10/14.
 */
public class Ball {

    public static int MIN_STRENGTH = 1;
    public static float MAX_STRENGTH = 200;
    public boolean tracking;
    public Vector2D edge;
    public Vector2D direction;
    private Vector2D _position;
    private float _strength;

    public Ball(float x, float y, float strength) {
        this(new Vector2D(x, y), strength);
    }

    public Ball(Vector2D position, float strength) {
        _position = position.getClone();
        _strength = strength;
        tracking = false;
        edge = position.getClone();
        direction = new Vector2D((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
    }

    public void position(Vector2D value) {
        _position.copy(value);
    }

    public void strength(float value) {
        _strength = MathUtil.clamp(value, MIN_STRENGTH, MAX_STRENGTH);
    }

    public float strengthAt(Vector2D v, float c) {
        float div = (float) Math.pow(Vector2D.subtract(_position, v).getLengthSq(), c * 0.5f);
        return (div != 0) ? (_strength / div) : 10000;
    }

    public String toString() {
        return "[Ball][position=" + position() + "][size=" + strength() + "]";
    }

    public float strength() {
        return _strength;
    }

    public Vector2D position() {
        return _position;
    }
}
