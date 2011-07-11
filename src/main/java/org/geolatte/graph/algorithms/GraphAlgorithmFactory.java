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
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph.algorithms;

import org.geolatte.graph.Locatable;
import org.geolatte.graph.LocateableGraph;
import org.geolatte.graph.Path;

import java.util.Map;

/**
 * @author Karel Maesen
 *         <p/>
 *         Creates {@link GraphAlgorithm}s.
 */
public class GraphAlgorithmFactory {

    public static GraphAlgorithmFactory instance = new GraphAlgorithmFactory();

    public <N extends Locatable, M> GraphAlgorithm<Map<N, Float>> createBFS(LocateableGraph<N> graph, N source, float maxDistance) {

        return new BFSDistanceLimited<N, M>(graph, source, maxDistance);
    }

    /**
     * Constructs a default Dijkstra shortest-path algorithm instance.
     *
     * @param graph       The graph on which to run the Dijkstra algorithm.
     * @param origin      The internalNode from which to start routing.
     * @param destination The destination internalNode to which to find a shortest path.
     * @param mode        The mode used to determine edge weight.
     * @param <N>         Type of nodes in the graph.
     * @param <M>         Type of mode object.
     * @return A default Dijkstra algorithm.
     */
    public <N extends Locatable, M> GraphAlgorithm<Path<N>> createDijkstra(LocateableGraph<N> graph,
                                                                           N origin,
                                                                           N destination,
                                                                           M mode) {

        return new Dijkstra<N, M>(graph, origin, destination, new DefaultRelaxer<N, M>(), mode);
    }

    /**
     * Constructs an A* shortest path algorithm with a straight-line distance heuristic.
     *
     * @param graph           The graph on which to run the Dijkstra algorithm.
     * @param origin          The internalNode from which to start routing.
     * @param destination     The destination internalNode to which to find a shortest path.
     * @param mode            The mode used to determine edge weight.
     * @param heuristicWeight The importance of the heuristic factor.
     * @param factor          Factor to convert distance to edge weights units.
     * @param <N>             Type of nodes in the graph.
     * @param <M>             Type of mode object.
     * @return An A* algorithm.
     */
    public <N extends Locatable, M> GraphAlgorithm<Path<N>> createAStar(LocateableGraph<N> graph,
                                                                        N origin,
                                                                        N destination,
                                                                        M mode,
                                                                        float heuristicWeight,
                                                                        float factor) {

        Relaxer<N, M> relaxer = this.createAStarRelaxer(heuristicWeight, factor, destination);
        return new Dijkstra<N, M>(graph, origin, destination, relaxer, mode);

    }

    /**
     * Constructs a default relaxer.
     *
     * @param <N> The internalNode type.
     * @param <M> The mode type.
     * @return A default relaxer.
     */
    protected <N extends Locatable, M> Relaxer<N, M> createDefaultRelaxer() {

        return new DefaultRelaxer<N, M>();
    }

    /**
     * Constructs a heuristic relaxer based on straight-line distance, used by the A* algorithm.
     *
     * @param heuristicWeight A weight to determine the relative importance of the heuristic value.
     * @param factor          Factor to convert distance to edge weights units.
     * @param destination     The destination node.
     * @param <N>             The node type.
     * @param <M>             The Modus.
     * @return A relaxer for the A* algoritmh
     */
    protected <N extends Locatable, M> Relaxer<N, M> createAStarRelaxer(float heuristicWeight, float factor, N destination) {

        return new HeuristicRelaxer<N, M>(heuristicWeight, destination, new DistanceHeuristicStrategy<N>(factor));
    }

}
