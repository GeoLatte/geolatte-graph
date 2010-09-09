package org.geolatte.graph;

import java.util.Set;

/**
 * A PredGraph that also keeps track of its successors.
 * <p/>
 * <p>
 * <i>Creation-Date</i>: 9-apr-2010<br>
 * <i>Creation-Time</i>:  11:48:54<br>
 * </p>
 *
 * @author Peter Rigole
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface PredSuccGraph<N extends Nodal> {

    PredSuccGraph<N> getPredecessor();

    void setPredecessor(PredSuccGraph<N> pred);

    float getWeight();

    void setWeight(float d);

    InternalNode<N> getInternalNode();

    Set<PredSuccGraph<N>> getSuccessors();

    void addSuccessor(PredSuccGraph<N> pred);

    void removeSuccessor(PredSuccGraph<N> pred);

}