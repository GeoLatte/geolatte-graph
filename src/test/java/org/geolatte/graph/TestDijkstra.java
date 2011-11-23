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

package org.geolatte.graph;

import org.geolatte.geom.Envelope;
import org.geolatte.geom.crs.CrsId;
import org.geolatte.graph.algorithms.GraphAlgorithm;
import org.geolatte.graph.algorithms.GraphAlgorithms;
import org.geolatte.stubs.MyLocatableNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDijkstra {

    static MyLocatableNode[] myNodes;
    static float[][] weights;

    static {
        myNodes = new MyLocatableNode[]{
                new MyLocatableNode(0, 0, 0),
                new MyLocatableNode(1, 100, 200),
                new MyLocatableNode(2, 100, 100),
                new MyLocatableNode(3, 200, 200),
                new MyLocatableNode(4, 200, 100)
        };

        weights = new float[][]{
                {-1f, 10f, 5f, -1f, -1f},
                {-1f, -1f, 2f, 1f, -1f},
                {-1f, 3f, -1f, 9f, 2f},
                {-1f, -1f, -1f, -1f, 4f},
                {7f, -1f, -1f, 6f, -1f}
        };
    }

    private MyLocatableNode copy(MyLocatableNode nd) {
        return new MyLocatableNode(nd.getID(), nd.getX(), nd.getY());
    }

    @Test
    public void testExecute() throws Exception {
        Envelope env = new Envelope(0d, 0d, 201d, 201d, CrsId.UNDEFINED);

        GraphBuilder<MyLocatableNode, Object> builder = Graphs.createGridIndexedGraphBuilder(env, 10);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (weights[i][j] > 0f) {
                    final int finalI = i;
                    final int finalJ = j;
                    builder.addEdge(copy(myNodes[i]), copy(myNodes[j]), new EdgeWeight() {
                        public float getValue(int weightKind) {
                            return weights[finalI][finalJ];
                        }
                    });
                }
            }
        }


        LocateableGraph<MyLocatableNode, Object> graph = builder.build();
        for (InternalNode<MyLocatableNode, Object> nd : graph) {
            System.out.println(nd);
        }

        GraphAlgorithm<Path<MyLocatableNode>> algorithm = GraphAlgorithms.createDijkstra(graph, myNodes[0], myNodes[3], 0);

        algorithm.execute();

        Path<MyLocatableNode> p = algorithm.getResult();
        System.out.println("Calculated path = " + p);

        assertTrue(p.isValid());
        assertTrue(p.getSource().equals(myNodes[0]));
        assertTrue(p.getDestination().equals(myNodes[3]));
        assertEquals(9f, p.totalWeight(), 0.01f);

    }
}

