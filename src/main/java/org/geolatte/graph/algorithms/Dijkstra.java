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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * Implements the basic Dijkstra shortest path algorithm. By passing in different relaxers, the algorithm can be
 * tweaked.
 * </p>
 *
 * @author Karel Maesen
 */
public class Dijkstra<N> implements GraphAlgorithm<Path<N>> {

    private final InternalNode<N> origin;
    private final InternalNode<N> destination;
    private final Graph<N> graph;
    private final int weightIndex;
    private Path<N> result;


    private final PMinQueue<N> minQueue;
    private final Relaxer<N> relaxer;
    private RoutingContextualReachability<N, Traversal<N>> reachability;

    protected Dijkstra(Graph<N> graph, N origin, N destination, Relaxer<N> relaxer, int weightIndex, RoutingContextualReachability<N, Traversal<N>> reachability) {

        this.graph = graph;

        this.origin = this.graph.getInternalNode(origin);
        this.destination = this.graph.getInternalNode(destination);
        this.weightIndex = weightIndex;
        this.relaxer = relaxer;
        this.minQueue = new PMinQueue<N>();
        this.reachability = reachability;
        this.reachability.setOriginDestination(this.origin.getWrappedNode(), this.destination.getWrappedNode());
    }

    protected Dijkstra(Graph<N> graph, N origin, N destination, Relaxer<N> relaxer, int weightIndex) {

        this(graph, origin, destination, relaxer, weightIndex, new EmptyContextualReachability<N, Traversal<N>>());
    }

    public void execute() {
        Set<InternalNode<N>> closed = new HashSet<InternalNode<N>>();
        BasicPredGraph<N> startPG = new BasicPredGraph<N>(this.origin, 0.0f);
        minQueue.add(startPG, Float.POSITIVE_INFINITY);
        while (!minQueue.isEmpty()) {
            PredGraph<N> pu = minQueue.extractMin();
            closed.add(pu.getInternalNode());
            if (isDone(pu)) {
                return;
            }
            InternalNode<N> u = pu.getInternalNode();
            reachability.setContext(pu);
            Iterator<InternalNode<N>> outEdges = graph.getOutGoingEdges(u, reachability);
            while (outEdges.hasNext()) {
                InternalNode<N> v = outEdges.next();
                if (closed.contains(v)) {
                    continue;
                }
                PredGraph<N> pv = minQueue.get(v);
                if (pv == null) {
                    pv = new BasicPredGraph<N>(v,
                            Float.POSITIVE_INFINITY);
                    minQueue.add(pv, Float.POSITIVE_INFINITY);
                }
                if (this.relaxer.relax(pu, pv, weightIndex)) {
                    this.minQueue.update(pv, this.relaxer.newTotalWeight());
                }
            }
        }
    }

    boolean isDone(PredGraph<N> pu) {
        if (pu.getInternalNode().equals(this.destination)) {
            this.result = toPath(pu);
            return true;
        }
        return false;
    }

    private Path<N> toPath(PredGraph<N> p) {
        BasicPath<N> path = new BasicPath<N>();
        path.setTotalWeight(p.getWeight());
        path.insert(p.getInternalNode().getWrappedNode());
        PredGraph<N> next = p.getPredecessor();

        while (next != null) {
            path.insert(next.getInternalNode().getWrappedNode());
            next = next.getPredecessor();
        }
        path.setValid(true);
        return path;
    }

    public Path<N> getResult() {
        return this.result;
    }
}
