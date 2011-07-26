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
public class Dijkstra<N, E> implements GraphAlgorithm<Path<N>> {

    private final InternalNode<N, E> origin;
    private final InternalNode<N, E> destination;
    private final Graph<N, E> graph;
    private final int weightIndex;
    private Path<N> result;


    private final PMinQueue<N, E> minQueue;
    private final Relaxer<N, E> relaxer;
    private RoutingContextualReachability<N, E, Traversal<N, E>> reachability;

    protected Dijkstra(Graph<N, E> graph, N origin, N destination, Relaxer<N, E> relaxer, int weightIndex, RoutingContextualReachability<N, E, Traversal<N, E>> reachability) {

        this.graph = graph;

        this.origin = this.graph.getInternalNode(origin);
        this.destination = this.graph.getInternalNode(destination);
        this.weightIndex = weightIndex;
        this.relaxer = relaxer;
        this.minQueue = new PMinQueue<N, E>();
        this.reachability = reachability;
        this.reachability.setOriginDestination(this.origin.getWrappedNode(), this.destination.getWrappedNode());
    }

    protected Dijkstra(Graph<N, E> graph, N origin, N destination, Relaxer<N, E> relaxer, int weightIndex) {

        this(graph, origin, destination, relaxer, weightIndex, new EmptyContextualReachability<N, E, Traversal<N, E>>());
    }

    public void execute() {
        Set<InternalNode<N, E>> closed = new HashSet<InternalNode<N, E>>();
        BasicPredGraph<N, E> startPG = new BasicPredGraph<N, E>(this.origin, 0.0f);
        minQueue.add(startPG, Float.POSITIVE_INFINITY);
        while (!minQueue.isEmpty()) {
            PredGraph<N, E> pu = minQueue.extractMin();
            closed.add(pu.getInternalNode());
            if (isDone(pu)) {
                return;
            }
            InternalNode<N, E> u = pu.getInternalNode();
            reachability.setContext(pu);
            Iterator<InternalNode<N, E>> outEdges = graph.getOutGoingEdges(u, reachability);
            while (outEdges.hasNext()) {
                InternalNode<N, E> v = outEdges.next();
                if (closed.contains(v)) {
                    continue;
                }
                PredGraph<N, E> pv = minQueue.get(v);
                if (pv == null) {
                    pv = new BasicPredGraph<N, E>(v,
                            Float.POSITIVE_INFINITY);
                    minQueue.add(pv, Float.POSITIVE_INFINITY);
                }
                if (this.relaxer.relax(pu, pv, weightIndex)) {
                    this.minQueue.update(pv, this.relaxer.newTotalWeight());
                }
            }
        }
    }

    boolean isDone(PredGraph<N, E> pu) {
        if (pu.getInternalNode().equals(this.destination)) {
            this.result = toPath(pu);
            return true;
        }
        return false;
    }

    private Path<N> toPath(PredGraph<N, E> p) {
        BasicPath<N> path = new BasicPath<N>();
        path.setTotalWeight(p.getWeight());
        path.insert(p.getInternalNode().getWrappedNode());
        PredGraph<N, E> next = p.getPredecessor();

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
