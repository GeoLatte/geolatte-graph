package org.geolatte.graph;

import java.util.Map;
import java.util.Set;

/**
 * @author Karel Maesen
 *         <p/>
 *         Creates {@link GraphAlgorithm}s.
 */
public class GraphAlgorithmFactory {

    public static GraphAlgorithmFactory instance = new GraphAlgorithmFactory();

    public <N extends Nodal, E extends EdgeLabel<M>, M> GraphAlgorithm<Map<N, Float>> createBFS(Graph<N, E, M> graph, N source, float maxDistance) {
        return new BFSDistanceLimited<N, E>(graph, source, maxDistance);
    }

    public <N extends Nodal, E extends EdgeLabel<M>, M> GraphAlgorithm<Path<N>> createDijkstra(Graph<N, E, M> graph, N origin,
                                                                       N destination, M modus) {
        return new Dijkstra<N, E, M>(graph, origin, destination, new DefaultRelaxer<N, E, M>(), modus);
    }


    public <N extends Nodal, E extends EdgeLabel<M>, M> GraphAlgorithm<Path<N>> createAStar(Graph<N, E, M> graph, N origin,
                                                                    N destination, float factor, float heuristicWeight, M modus) {

        Relaxer<N, E, M> relaxer = this.createAStarRelaxer(heuristicWeight, factor,
                destination);
        return new Dijkstra<N, E, M>(graph, origin, destination, relaxer, modus);

    }

//    public <N extends Nodal, E> GraphAlgorithm<Set<InternalNode<N>>> createCoverage(Graph<N, E> graph, N origin,
//                                                                               float maxDistance){
//        return new Coverage<N, E>(graph, origin, new DefaultRelaxer<N, E>(), maxDistance);
//    }

    protected <N extends Nodal, E extends EdgeLabel<M>, M> Relaxer<N, E, M> createDefaultRelaxer() {
        return new DefaultRelaxer<N, E, M>();
    }

    protected <N extends Nodal, E extends EdgeLabel<M>, M> Relaxer<N, E, M> createAStarRelaxer(float heuristicWeight, float factor,
                                                                    N destination) {
        return new HeuristicRelaxer<N, E, M>(heuristicWeight, factor, destination);
    }


    protected static class DefaultRelaxer<N extends Nodal, E extends EdgeLabel<M>, M> implements Relaxer<N, E, M> {

        float newWeight;

        /**
         * 
         * @param u PredGraph representing current shortest path to node n_u
         * @param v PredGraph representing current shortest path to node n_v
         * @param edge Edge from node n_u to node n_v
         * @param weight The weight of the edge from node n_u to node n_v
         * @return True if the weight of PredGraph v was updated, false otherwise
         */
        public boolean relax(PredGraph<N> u, PredGraph<N> v, E edgeLabel, M modus) {
            float r = u.getWeight() + edgeLabel.getWeight(modus);
            if (v.getWeight() > r) {
                v.setWeight(r);
                v.setPredecessor(u);
                newWeight = update(v.getInternalNode(), v.getWeight());
                return true;
            } else {
                newWeight = v.getWeight();
                return false;
            }
        }


        protected float update(InternalNode<N> nd, float distance) {
            return distance;
        }


        public float newTotalWeight() {
            return newWeight;
        }

    }

    protected static class HeuristicRelaxer<N extends Nodal, E extends EdgeLabel<M>, M> extends DefaultRelaxer<N, E, M> {

        private final float heuristicWeight; // weight given to the heuristic
        // component
        private final float factor; // factor for distance
        private final N destination;

        private HeuristicRelaxer(float heuristicWeight, float factor,
                                 N destination) {
            this.heuristicWeight = heuristicWeight;
            this.factor = factor;
            this.destination = destination;
        }

        protected float update(InternalNode<N> nd, float weight) {
            return weight + this.heuristicWeight * this.factor
                    * (getDistance(nd, this.destination));
        }

        protected float getDistance(Nodal f, Nodal t) {
            double dx = (double) (f.getX() - t.getX());
            double dy = (double) (f.getY() - t.getY());
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

    }

}
