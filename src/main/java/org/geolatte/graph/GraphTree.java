package org.geolatte.graph;

import java.util.List;
import java.util.Map;

/**
 * No comment provided yet for this class.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 3/12/11<br>
 * <i>Creation-Time</i>: 15:20<br>
 * </p>
 *
 * @author Peter Rigole
 * @since SDK1.5
 */
public interface GraphTree<N, E> {


    GraphTreeIterator<N,E> iterator();


    N getRoot();

    double getRootDistance();

    List<GraphTree<N, E>> getChildren();

    Map<N, Double> toMap();

}