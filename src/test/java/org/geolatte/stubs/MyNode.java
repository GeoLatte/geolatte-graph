package org.geolatte.stubs;

/**
 * <p>
 * No comment provided yet for this class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class MyNode {

    private final int id;

    public MyNode(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public Object getData() {
        return null;
    }

    public String toString() {
        return String.format("%d", this.id);
    }

    /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MyNode))
            return false;
        MyNode other = (MyNode) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
