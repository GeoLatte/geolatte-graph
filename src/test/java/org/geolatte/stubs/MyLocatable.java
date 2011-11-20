package org.geolatte.stubs;

import org.geolatte.graph.Locatable;

/**
 * <p>
 * No comment provided yet for this class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class MyLocatable implements Locatable {


    private float x;
    private float y;

    public MyLocatable(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return The X-coordinate
     */
    public float getX() {
        return this.x;
    }

    /**
     * @return The Y-coordinate
     */
    public float getY() {
        return this.y;
    }
}
