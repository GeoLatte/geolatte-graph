package org.geolatte.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Dijkstra extension to compute the coverage from a given origin and a maximum distance value.
 * The stop-criterium in this algorithm differs from Dijkstra in that it only stops when all non-visited nodes have
 * a value that is larger than the requested maximum distance. The result is not a path, but a set
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
public class Coverage<N extends Nodal, E> implements GraphAlgorithm<Set<InternalNode<N>>> {

    private DijkstraCoverage dijkstraCoverage;
    private Set<InternalNode<N>> result;
    private final Graph<N, E> graph;

    protected Coverage(Graph<N, E> graph, N origin, Relaxer<N, E> relaxer, float maxDistance) {
        dijkstraCoverage = new DijkstraCoverage(graph, origin, relaxer, maxDistance);
        this.graph = graph;
    }

    public void execute() {
        dijkstraCoverage.execute();
    }

    public Set<InternalNode<N>> getResult() {
        result = new HashSet<InternalNode<N>>(dijkstraCoverage.getClosed());
        // The result always has to add one node beyond the current edge nodes where possible, because a correct
        // coverage needs to be able interpolate the last two points on each path that reaches the edge
        // of the coverage. E.g. if the requested distance is 1000, then a point on the edge of the
        // coverage cannot be 900, so it has to include its neighbors that surpass the 1000 limit in order
        // to be able to interpolate between 900 and its neighbors that go beyond the 1000 limit. Obviously,
        // this doesn't count when the 900 edge doesn't have neighboring edges that go beyond the 1000 limit.
        Set<InternalNode<N>> toAdd = new HashSet<InternalNode<N>>();
        for (InternalNode<N> node : result) {
            OutEdgeIterator<N, E> iterator = graph.getOutGoingEdges(node);
            while (iterator.next()) {
                // Mind that this is an expensive operation. Any suggestions to optimize?
                InternalNode<N> neighborNode = iterator.getToInternalNode();
                if (!result.contains(neighborNode)) {
                    if (!toAdd.contains(neighborNode) && neighborNode.getValue() != 0.0f) {
                        // Who can tell me why neighborNode.getValue() is sometimes == 0.0f?
                        toAdd.add(neighborNode);
                    }
                }
            }
        }
        for (InternalNode<N> nodeToAdd : toAdd) {
            result.add(nodeToAdd);
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
                return true;
            }
            return false;
        }
    }

}