package org.geolatte.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Dijkstra extension to compute the coverage from a given origin and a maximum distance value.
 * The stop-criterium in this algorithm differs from Dijkstra in that it only stops when all non-visited nodes have
 * a value that is larger than the requested maximum distance. The result is not a path, but rather a graph
 * containing all nodes that are reachable from the given origin within the given maximum travel distance.
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
public class Coverage<N extends Nodal, E> implements GraphAlgorithm<List<InternalNode<N>>> {

    private DijkstraCoverage dijkstraCoverage;

    protected Coverage(Graph<N, E> graph, N origin, Relaxer<N, E> relaxer, float maxDistance) {
        dijkstraCoverage = new DijkstraCoverage(graph, origin, relaxer, maxDistance);
    }

    public void execute() {
        dijkstraCoverage.execute();
    }

    public List<InternalNode<N>> getResult() {
        List<InternalNode<N>> result = new ArrayList<InternalNode<N>>();
        while (!dijkstraCoverage.getMinQueue().isEmpty()) {
            PredGraph<N> predGraph = dijkstraCoverage.getMinQueue().extractMin();
            while (predGraph != null) {
                InternalNode<N> iNode = predGraph.getInternalNode();
                if (!result.contains(iNode)) {
                    iNode.setValue(predGraph.getWeight());
                    result.add(iNode);
                    predGraph = predGraph.getPredecessor();
                } else {
                    // If the result already contains the current iNode, then it must also contain all
                    // its predecessors already.
                    predGraph = null;
                }
            }
        }
        return result;
    }

    static class DijkstraCoverage<N extends Nodal, E> extends Dijkstra<N, E> {

        private float maxDistance;

        protected DijkstraCoverage(Graph<N, E> graph, N origin, Relaxer<N, E> relaxer, float maxDistance) {
            super(graph, origin, null, relaxer);
            this.maxDistance = maxDistance;
        }

        protected boolean isDone(PredGraph<N> pu) {
            if (pu.getWeight() > maxDistance) {
                getMinQueue().add(pu, pu.getWeight());
                return true;
            }
            return false;
        }
    }

}