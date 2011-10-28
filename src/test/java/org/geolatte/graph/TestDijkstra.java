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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDijkstra {

    static MyNode[] myNodes;
    static float[][] weights;

    static {
        myNodes = new MyNode[]{
                new MyNode(0, 0, 0),
                new MyNode(1, 100, 200),
                new MyNode(2, 100, 100),
                new MyNode(3, 200, 200),
                new MyNode(4, 200, 100)
        };

        weights = new float[][]{
                {-1f, 10f, 5f, -1f, -1f},
                {-1f, -1f, 2f, 1f, -1f},
                {-1f, 3f, -1f, 9f, 2f},
                {-1f, -1f, -1f, -1f, 4f},
                {7f, -1f, -1f, 6f, -1f}
        };
    }

    private MyNode copy(MyNode nd) {
        return new MyNode(nd.getID(), nd.getX(), nd.getY());
    }

    @Test
    public void testExecute() throws Exception {
        Envelope env = new Envelope(0d, 0d, 201d, 201d, CrsId.UNDEFINED);

        GraphBuilder<MyNode, Object> builder = Graphs.createGridIndexedGraphBuilder(env, 10);
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


        LocateableGraph<MyNode, Object> graph = builder.build();
        for (InternalNode<MyNode, Object> nd : graph) {
            System.out.println(nd);
        }

        GraphAlgorithm<Path<MyNode>> algorithm = GraphAlgorithms.createDijkstra(graph, myNodes[0], myNodes[3], 0);

        algorithm.execute();

        Path<MyNode> p = algorithm.getResult();
        System.out.println("Calculated path = " + p);

        assertTrue(p.isValid());
        assertTrue(p.getSource().equals(myNodes[0]));
        assertTrue(p.getDestination().equals(myNodes[3]));
        assertEquals(9f, p.totalWeight(), 0.01f);

    }

    private static class MyNode implements Locatable {

        private final int id;
        private final float x;
        private final float y;

        protected MyNode(int id, float x, float y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getID() {
            return this.id;
        }

        public float getX() {
            return this.x;
        }


        public float getY() {
            return this.y;
        }

        public Object getData() {
            return null;
        }

        public String toString() {
            return String.format("%d: (%f, %f)", this.id, this.x, this.y);
        }

        /* (non-Javadoc)
           * @see java.lang.Object#hashCode()
           */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + (int) x;
            result = prime * result + (int) y;
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
            if (!(obj instanceof MyNode))
                return false;
            MyNode other = (MyNode) obj;
            if (id != other.id)
                return false;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }


    }

}

