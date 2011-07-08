package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;
import org.geolatte.graph.algorithms.GraphAlgorithm;
import org.geolatte.graph.algorithms.GraphAlgorithmFactory;
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
                {-1f, 10f,  5f, -1f, -1f},
                {-1f, -1f,  2f,  1f, -1f},
                {-1f,  3f, -1f,  9f,  2f},
                {-1f, -1f, -1f, -1f,  4f},
                { 7f, -1f, -1f,  6f, -1f}
        };
    }

    private MyNode copy(MyNode nd) {
        return new MyNode(nd.getID(), nd.getX(), nd.getY());
    }

    @Test
    public void testExecute() throws Exception {
        Envelope env = new Envelope(0, 200, 0, 200);

        GraphBuilder<MyNode> builder = Graphs.<MyNode>createGridIndexedGraphBuilder(env, 10);
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


        Graph<MyNode> graph = builder.build();
        for (LocatedNode<MyNode> nd : graph) {
            System.out.println(nd);
        }

        GraphAlgorithm<Path<MyNode>> algorithm = GraphAlgorithmFactory.instance.createDijkstra(graph, myNodes[0], myNodes[3], new MyMode());

        algorithm.execute();

        Path<MyNode> p = algorithm.getResult();
        System.out.println("Calculated path = " + p);

        assertTrue(p.isValid());
        assertTrue(p.getSource().equals(myNodes[0]));
        assertTrue(p.getDestination().equals(myNodes[3]));
        assertEquals(9f, p.totalWeight(), 0.01f);

    }

    private static class MyMode {
        
    }

    private static class MyNode implements Located {

        private final int id;
        private final int x;
        private final int y;

        protected MyNode(int id, int x, int y) {
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

