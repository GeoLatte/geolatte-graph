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

import org.geolatte.graph.*;

import java.util.Map;

/**
 * @author Karel Maesen
 *         <p/>
 *         Creates {@link GraphAlgorithm}s.
 */
public class GraphAlgorithmFactory {

    public static GraphAlgorithmFactory instance = new GraphAlgorithmFactory();

    public <N extends Nodal, M> GraphAlgorithm<Map<N, Float>> createBFS(Graph<N> graph, N source, float maxDistance, EdgeWeightCalculator<N, M> edgeWeightCalculator) {

        // TODO: must give a modus
        return new BFSDistanceLimited<N, M>(graph, source, maxDistance, null, edgeWeightCalculator);
    }

    public <N extends Nodal, M> GraphAlgorithm<Path<N>> createDijkstra(Graph<N> graph,
                                                                       N origin,
                                                                       N destination,
                                                                       M modus,
                                                                       EdgeWeightCalculator<N, M> edgeWeightCalculator) {

        return new Dijkstra<N, M>(graph, origin, destination, new DefaultRelaxer<N, M>(), modus, edgeWeightCalculator);
    }


    // TODO: Temporarly commented out because of compilation problems I don't get
    /*
    public <N extends Nodal, E extends EdgeWeightCalculator<M>, M> GraphAlgorithm<Path<N>> createAStar(Graph<N, E, M> graph, N origin,
                                                                    N destination, float factor, float heuristicWeight, M modus) {

        Relaxer<N, E, M> relaxer = this.createAStarRelaxer(heuristicWeight, factor, destination);
        return new Dijkstra<N, E, M>(graph, origin, destination, relaxer, modus);

    }
    */

//    public <N extends Nodal, E> GraphAlgorithm<Set<InternalNode<N>>> createCoverage(Graph<N, E> graph, N origin,
//                                                                               float maxDistance){
//        return new Coverage<N, E>(graph, origin, new DefaultRelaxer<N, E>(), maxDistance);
//    }

    protected <N extends Nodal, M> Relaxer<N, M> createDefaultRelaxer() {

        return new DefaultRelaxer<N, M>();
    }

    protected <N extends Nodal, M> Relaxer<N, M> createAStarRelaxer(float heuristicWeight, float factor, N destination) {
        
        return new HeuristicRelaxer<N, M>(heuristicWeight, factor, destination);
    }

}
