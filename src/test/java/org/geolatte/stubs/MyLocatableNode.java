package org.geolatte.stubs;

import org.geolatte.graph.Locatable;

public class MyLocatableNode extends MyNode implements Locatable {

        private final float x;
        private final float y;

        public MyLocatableNode(int id, float x, float y) {
            super(id);
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return this.x;
        }


        public float getY() {
            return this.y;
        }

        public Object getData() {
            return null;
        }

        public String toString() {
            return String.format("%d: (%f, %f)", getID(), this.x, this.y);
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyLocatableNode)) return false;
        if (!super.equals(o)) return false;

        MyLocatableNode that = (MyLocatableNode) o;

        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}