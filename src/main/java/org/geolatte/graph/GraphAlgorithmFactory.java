package org.geolatte.graph;

import java.util.Map;

/**
 * @author Karel Maesen
 *         <p/>
 *         Creates {@link GraphAlgorithm}s.
 */
public class GraphAlgorithmFactory {

    public static GraphAlgorithmFactory instance = new GraphAlgorithmFactory();

    public <N extends Nodal, E> GraphAlgorithm<Map<N, Float>> createBFS(Graph<N, E> graph, N source, float maxDistance) {
        return new BFSDistanceLimited<N, E>(graph, source, maxDistance);
    }

    public <N extends Nodal, E> GraphAlgorithm<Path<N>> createDijkstra(Graph<N, E> graph, N origin,
                                                                       N destination) {
        return new Dijkstra<N, E>(graph, origin, destination, new DefaultRelaxer<N, E>());
    }


    public <N extends Nodal, E> GraphAlgorithm<Path<N>> createAStar(Graph<N, E> graph, N origin,
                                                                    N destination, float factor, float heuristicWeight) {

        Relaxer<N, E> relaxer = this.createAStarRelaxer(heuristicWeight, factor,
                destination);
        return new Dijkstra<N, E>(graph, origin, destination, relaxer);

    }

    protected <N extends Nodal, E> Relaxer<N, E> createDefaultRelaxer() {
        return new DefaultRelaxer<N, E>();
    }

    protected <N extends Nodal, E> Relaxer<N, E> createAStarRelaxer(float heuristicWeight, float factor,
                                                                    N destination) {
        return new HeuristicRelaxer<N, E>(heuristicWeight, factor, destination);
    }


    protected static class DefaultRelaxer<N extends Nodal, E> implements Relaxer<N, E> {

        float newWeight;


        public boolean relax(PredGraph<N> u, PredGraph<N> v, E edge, float weight) {
            float r = u.getWeight() + weight;
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

    protected static class HeuristicRelaxer<N extends Nodal, E> extends DefaultRelaxer<N, E> {

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
