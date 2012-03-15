package org.geolatte.graph;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 3/12/11<br>
 * <i>Creation-Time</i>: 16:29<br>
 * </p>
 *
 * @author Peter Rigole
 * @since SDK1.5
 */
public interface GraphTreeIterator<N,E> {

    boolean next();

    double getCurrentDistance();

    N getCurrentNode();

    E getCurrentEdge();


}