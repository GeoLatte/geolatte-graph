package org.geolatte.graph;


/**
 * <code>Nodal</code> captures minimum requirements on the nodes
 * in a <code>Graph</code>.
 *
 * <p><em>Implementations should be thread-safe</em>. The <code>getData()</code>-method
 * is intended to be used concurrently by threads executing some graph algorithm.</p>
 *
 * @author Karel Maesen, Geovise BVBA
 *
 *
 */
public interface Nodal<D> {

    /**
     * Returns The X-coordinate of the node
     * @return X-coordinate
     */
	public int getX();

    /**
     * Returns the Y-coordinate of the node
     * @return Y-coordinate
     */
	public int getY();

//    /**
//     * Returns the
//     * @return
//     */
//	public Envelope getEnvelope();

    /**
     * Returns application-specific data.
     * @return
     */
    public D getData();


}
