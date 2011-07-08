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

import java.util.*;

/**
 * <p>
 * Breath-first search algorithm that over a given maximum distance. (Documentation to be completed)
 * <p>
 *
 * @param <N>
 * @param <M>
 */
public class BFSDistanceLimited<N, M> implements GraphAlgorithm<Map<N, Float>> {

    final Set<BFSState<N>> blackNodes = new HashSet<BFSState<N>>();
    final Queue<BFSState<N>> greyNodes = new LinkedList<BFSState<N>>();
    final InternalNode<N> source;
    final float maxDistance;
    private final M modus;
    final Graph<N> graph;
    Map<N, Float> result;

    protected BFSDistanceLimited(Graph<N> graph, N source, float maxDistance, M modus) {
        this.graph = graph;
        this.source = graph.getInternalNode(source);
        this.maxDistance = maxDistance;
        this.modus = modus;
    }


    public void execute() {

        BFSState<N> ws = new BFSState<N>(this.source);
        ws.distance = 0.f;
        ws.predecessor = null;
        greyNodes.add(ws);

        while (!greyNodes.isEmpty()) {
            BFSState<N> wu = this.greyNodes.remove();

            if (wu.distance > maxDistance) {
                continue; //don't expand when the internalNode is beyond maximum distance.
            }

            Iterator<InternalNode<N>> outEdges = this.graph.getOutGoingEdges(wu.internalNode, null); // TODO: context reachability
            while (outEdges.hasNext()) {
                InternalNode<N> v = outEdges.next();
                BFSState<N> wv = new BFSState<N>(v);
                if (greyNodes.contains(wv) || blackNodes.contains(wv)) {
                    continue; // do nothing
                } else {
                    // TODO: correct doorgeven van wieghtKind
                    wv.distance = wu.distance + wu.internalNode.getWeightTo(v, 0);
                    wv.predecessor = wu.internalNode;
                    greyNodes.add(wv);
                }
            }
            blackNodes.add(wu);
        }

        this.result = toMap(blackNodes);

    }


    public Map<N, Float> getResult() {
        return this.result;
    }

    protected Map<N, Float> toMap(Set<BFSState<N>> nodes) {
        Map<N, Float> map = new HashMap<N, Float>(nodes.size());
        for (BFSState<N> wu : nodes) {
            map.put(wu.internalNode.getWrappedNode(), wu.distance);
        }
        return map;

    }

    private static class BFSState<N> {

        final InternalNode<N> internalNode;
        float distance = Float.POSITIVE_INFINITY;
        InternalNode<N> predecessor;

        private BFSState(InternalNode<N> internalNode) {
            this.internalNode = internalNode;
        }

        /* (non-Javadoc)
           * @see java.lang.Object#hashCode()
           */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((internalNode == null) ? 0 : internalNode.hashCode());
            return result;
        }

        /* (non-Javadoc)
           * @see java.lang.Object#equals(java.lang.Object)
           */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof BFSState))
                return false;
            BFSState other = (BFSState) obj;
            if (internalNode == null) {
                if (other.internalNode != null)
                    return false;
            } else if (!internalNode.equals(other.internalNode))
                return false;
            return true;
        }


    }

}
