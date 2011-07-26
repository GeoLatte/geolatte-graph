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

import org.geolatte.graph.*;

import java.util.Map;

/**
 * Offers a number of static factory methods to instantiate pre-configured graph algorithms.
 *
 * @author Karel Maesen
 * @author Bert Vanhooff
 */
public class GraphAlgorithms {

    /**
     * Creates a {@link BFSDistanceLimited} algorithm instance.
     *
     * @param graph       The graph.
     * @param source      The source node.
     * @param maxDistance The maximum distance to search in.
     * @param weightIndex The index to lookup the weight.
     * @param <N>         The type of domain node.
     * @param <E>         The edge label type.
     * @return A ready-to-use BFS algorithm.
     */
    public static <N extends Locatable, E> GraphAlgorithm<Map<N, Float>> createBFS(LocateableGraph<N, E> graph,
                                                                                   N source,
                                                                                   float maxDistance,
                                                                                   int weightIndex) {

        return new BFSDistanceLimited<N, E>(graph, source, maxDistance, weightIndex);
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
    public static <N, E> GraphAlgorithm<Path<N>> createDijkstra(Graph<N, E> graph,
                                                                N origin,
                                                                N destination,
                                                                int weightKind) {

        return new Dijkstra<N, E>(graph, origin, destination, GraphAlgorithms.<N, E>createDefaultRelaxer(), weightKind);
    }

    /**
     * Constructs a Dijkstra shortest-path algorithm instance.
     *
     * @param graph                  The graph on which to run the Dijkstra algorithm.
     * @param origin                 The internalNode from which to start routing.
     * @param destination            The destination internalNode to which to find a shortest path.
     * @param weightKind             The index to lookup the weight.
     * @param contextualReachability Object that determines node reachability on the fly.
     * @param <N>                    Type of nodes in the graph.
     * @param <E>                    The edge label type.
     * @return A default Dijkstra algorithm.
     */
    public static <N, E> GraphAlgorithm<Path<N>> createDijkstra(Graph<N, E> graph,
                                                                N origin,
                                                                N destination,
                                                                int weightKind,
                                                                RoutingContextualReachability<N, E, Traversal<N, E>> contextualReachability) {

        return new Dijkstra<N, E>(graph, origin, destination, GraphAlgorithms.<N, E>createDefaultRelaxer(), weightKind, contextualReachability);
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
     * @param <E>             The edge label type.
     * @return An A* algorithm.
     */
    public static <N extends Locatable, E> GraphAlgorithm<Path<N>> createAStar(LocateableGraph<N, E> graph,
                                                                               N origin,
                                                                               N destination,
                                                                               int weightIndex,
                                                                               float heuristicWeight,
                                                                               float factor) {

        Relaxer<N, E> relaxer = createAStarRelaxer(heuristicWeight, factor, destination);
        return new Dijkstra<N, E>(graph, origin, destination, relaxer, weightIndex);

    }

    /**
     * Constructs a default relaxer.
     *
     * @param <N> The internalNode type.
     * @param <E> The edge label type.
     * @return A default relaxer.
     */
    protected static <N, E> Relaxer<N, E> createDefaultRelaxer() {

        return new DefaultRelaxer<N, E>();
    }

    /**
     * Constructs a heuristic relaxer based on straight-line distance, used by the A* algorithm.
     *
     * @param heuristicWeight A weight to determine the relative importance of the heuristic value.
     * @param factor          Factor to convert distance to edge weights units.
     * @param destination     The destination node.
     * @param <N>             The node type.
     * @param <E>             The edge label type.
     * @return A relaxer for the A* algorithms.
     */
    protected static <N extends Locatable, E> Relaxer<N, E> createAStarRelaxer(float heuristicWeight, float factor, N destination) {

        return new HeuristicRelaxer<N, E>(heuristicWeight, destination, new DistanceHeuristicStrategy<N>(factor));
    }

}
