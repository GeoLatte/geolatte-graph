package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDijkstra {

    static Node[] nodes;
    static float[][] weights;

    static {
        nodes = new Node[]{
                new Node(0, 0, 0),
                new Node(1, 100, 200),
                new Node(2, 100, 100),
                new Node(3, 200, 200),
                new Node(4, 200, 100)
        };

        weights = new float[][]{
                {-1f, 10f, 5f, -1f, -1f},
                {-1f, -1f, 2f, 1f, -1f},
                {-1f, 3f, -1f, 9f, 2f},
                {-1f, -1f, -1f, -1f, 4f},
                {7f, -1f, -1f, 6f, -1f}
        };
    }

    private Node copy(Node nd) {
        return new Node(nd.getID(), nd.getX(), nd.getY());
    }

    @Test
    public void testExecute() throws Exception {
        Envelope env = new Envelope(0, 200, 0, 200);

        GraphBuilder<Node, EdgeWeightImpl> builder = Graphs.createGridIndexedGraphBuilder(env, 10);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (weights[i][j] > 0f) {
                    builder.addEdge(copy(nodes[i]), copy(nodes[j]), new EdgeWeightImpl(weights[i][j]), weights[i][j]);
                }
            }
        }


        Graph<Node, EdgeWeightImpl> graph = builder.build();
        for (Node nd : graph) {
            System.out.println(nd);
        }

        GraphAlgorithm<Path<Node>> algorithm = GraphAlgorithmFactory.instance.createDijkstra(graph, nodes[0], nodes[3]);

        algorithm.execute();

        Path<Node> p = algorithm.getResult();
        System.out.println("Calculated path = " + p);

        assertTrue(p.isValid());
        assertTrue(p.getSource().equals(nodes[0]));
        assertTrue(p.getDestination().equals(nodes[3]));
        assertEquals(9f, p.totalWeight(), 0.01f);

    }


    private static class EdgeWeightImpl {

        private float w;

        private EdgeWeightImpl(float w) {
            this.w = w;
        }


        public float getMaxConstraint() {
            // TODO Auto-generated method stub
            return 0;
        }


        public float getMinConstraint() {
            // TODO Auto-generated method stub
            return 0;
        }


        public float getWeight() {
            return this.w;
        }


    }

    private static class Node implements Nodal {

        private final int id;
        private final int x;
        private final int y;

        protected Node(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getID() {
            return this.id;
        }


        public Envelope getEnvelope() {
            return new Envelope(this.x, this.x, this.y, this.y);
        }


        public int getX() {
            return this.x;
        }


        public int getY() {
            return this.y;
        }

        public Object getData() {
            return null; 
        }

        public String toString() {
            return String.format("%d: (%d,%d)", this.id, this.x, this.y);
        }

        /* (non-Javadoc)
           * @see java.lang.Object#hashCode()
           */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + x;
            result = prime * result + y;
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
            if (!(obj instanceof Node))
                return false;
            Node other = (Node) obj;
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

