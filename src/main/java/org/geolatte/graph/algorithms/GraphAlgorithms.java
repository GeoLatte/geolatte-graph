/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Esperantolaan 4 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.Graph;
import org.geolatte.graph.Locatable;
import org.geolatte.graph.LocateableGraph;
import org.geolatte.graph.Path;

import java.util.Map;

/**
 * Offers a number of static factory methods to instantiate pre-configured graph algorithms.
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public class GraphAlgorithms {

    public static GraphAlgorithms instance = new GraphAlgorithms();

    /**
     * Creates a {@link BFSDistanceLimited} algorithm instance.
     *
     * @param graph       The graph.
     * @param source      The source node.
     * @param maxDistance The maximum distance to search in.
     * @param weightIndex The index to lookup the weight.
     * @param <N>         The type of domain node.
     * @return A ready-to-use BFS algorithm.
     */
    public <N extends Locatable> GraphAlgorithm<Map<N, Float>> createBFS(LocateableGraph<N> graph, N source, float maxDistance, int weightIndex) {

        return new BFSDistanceLimited<N>(graph, source, maxDistance, weightIndex);
    }

    /**
     * Constructs a Dijkstra shortest-path algorithm instance.
     *
     * @param graph       The graph on which to run the Dijkstra algorithm.
     * @param origin      The internalNode from which to start routing.
     * @param destination The destination internalNode to which to find a shortest path.
     * @param weightKind  The index to lookup the weight.
     * @param <N>         Type of nodes in the graph.
     * @return A default Dijkstra algorithm.
     */
    public <N extends Locatable> GraphAlgorithm<Path<N>> createDijkstra(Graph<N> graph,
                                                                        N origin,
                                                                        N destination,
                                                                        int weightKind) {

        return new Dijkstra<N>(graph, origin, destination, this.<N>createDefaultRelaxer(), weightKind);
    }

    /**
     * Constructs an A* shortest path algorithm with a straight-line distance heuristic.
     *
     * @param graph           The graph on which to run the Dijkstra algorithm.
     * @param origin          The internalNode from which to start routing.
     * @param destination     The destination internalNode to which to find a shortest path.
     * @param weightIndex     The index to lookup the weight
     * @param heuristicWeight The importance of the heuristic factor.
     * @param factor          Factor to convert distance to edge weights units.
     * @param <N>             Type of nodes in the graph.
     * @return An A* algorithm.
     */
    public <N extends Locatable> GraphAlgorithm<Path<N>> createAStar(LocateableGraph<N> graph,
                                                                     N origin,
                                                                     N destination,
                                                                     int weightIndex,
                                                                     float heuristicWeight,
                                                                     float factor) {

        Relaxer<N> relaxer = this.createAStarRelaxer(heuristicWeight, factor, destination);
        return new Dijkstra<N>(graph, origin, destination, relaxer, weightIndex);

    }

    /**
     * Constructs a default relaxer.
     *
     * @param <N> The internalNode type.
     * @return A default relaxer.
     */
    protected <N> Relaxer<N> createDefaultRelaxer() {

        return new DefaultRelaxer<N>();
    }

    /**
     * Constructs a heuristic relaxer based on straight-line distance, used by the A* algorithm.
     *
     * @param heuristicWeight A weight to determine the relative importance of the heuristic value.
     * @param factor          Factor to convert distance to edge weights units.
     * @param destination     The destination node.
     * @param <N>             The node type.
     * @return A relaxer for the A* algorithms.
     */
    protected <N extends Locatable> Relaxer<N> createAStarRelaxer(float heuristicWeight, float factor, N destination) {

        return new HeuristicRelaxer<N>(heuristicWeight, destination, new DistanceHeuristicStrategy<N>(factor));
    }

}
