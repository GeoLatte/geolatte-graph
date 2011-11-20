package org.geolatte.graph.algorithms;

import org.geolatte.geom.Envelope;
import org.geolatte.graph.BasicEdgeWeight;
import org.geolatte.graph.Graph;
import org.geolatte.graph.GraphBuilder;
import org.geolatte.graph.Graphs;
import org.geolatte.stubs.MyLocatableNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * Tests the {@link BFSDistanceLimited} class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class BFSDistanceLimitedTest {

    /**
     * We use this graph for testing.
     * edge weights: 1
     * cumulative node weights: (1)
     * node ids: _1
     * all edges are added for both directions
     *
     *      _1      _2      _3      _4      _5
     *      (1)--1--(2)--1--(3)--1--(2)--1--(3)
     *      /                        /
     *     1                        /
     *    /                      _6/      _7      _8      _9
     * _0 o------------1----------(1)--1--(2)--1--(3)--1--(4)
     *    \ \
     *     \ 2
     *      3 \ _10
     *       \ (2)
     *       (3)
     *       _11
     *
     */

    private MyLocatableNode _0  = new MyLocatableNode(0, 0, 0);
    private MyLocatableNode _1  = new MyLocatableNode(1, 0, 0);
    private MyLocatableNode _2  = new MyLocatableNode(2, 0, 0);
    private MyLocatableNode _3  = new MyLocatableNode(3, 0, 0);
    private MyLocatableNode _4  = new MyLocatableNode(4, 0, 0);
    private MyLocatableNode _5  = new MyLocatableNode(5, 0, 0);
    private MyLocatableNode _6  = new MyLocatableNode(6, 0, 0);
    private MyLocatableNode _7  = new MyLocatableNode(7, 0, 0);
    private MyLocatableNode _8  = new MyLocatableNode(8, 0, 0);
    private MyLocatableNode _9  = new MyLocatableNode(9, 0, 0);
    private MyLocatableNode _10 = new MyLocatableNode(10, 0, 0);
    private MyLocatableNode _11 = new MyLocatableNode(11, 0, 0);

    private Graph<MyLocatableNode, Object> graph;

    @Before
    public void setup() throws Exception {

        Envelope env = new Envelope(0d, 0d, 200d, 200d);

        GraphBuilder<MyLocatableNode, Object> builder = Graphs.createGridIndexedGraphBuilder(env, 10);

        builder.addEdge(_0, _1, new BasicEdgeWeight(1));
        builder.addEdge(_1, _2, new BasicEdgeWeight(1));
        builder.addEdge(_2, _3, new BasicEdgeWeight(1));
        builder.addEdge(_3, _4, new BasicEdgeWeight(1));
        builder.addEdge(_4, _5, new BasicEdgeWeight(1));
        builder.addEdge(_0, _6, new BasicEdgeWeight(1));
        builder.addEdge(_6, _4, new BasicEdgeWeight(1));
        builder.addEdge(_6, _7, new BasicEdgeWeight(1));
        builder.addEdge(_7, _8, new BasicEdgeWeight(1));
        builder.addEdge(_8, _9, new BasicEdgeWeight(1));
        builder.addEdge(_0, _10, new BasicEdgeWeight(2));
        builder.addEdge(_0, _11, new BasicEdgeWeight(3));

        // Add same edges in other direction
        builder.addEdge(_1, _0, new BasicEdgeWeight(1));
        builder.addEdge(_2, _1, new BasicEdgeWeight(1));
        builder.addEdge(_3, _2, new BasicEdgeWeight(1));
        builder.addEdge(_4, _3, new BasicEdgeWeight(1));
        builder.addEdge(_5, _4, new BasicEdgeWeight(1));
        builder.addEdge(_6, _0, new BasicEdgeWeight(1));
        builder.addEdge(_4, _6, new BasicEdgeWeight(1));
        builder.addEdge(_7, _6, new BasicEdgeWeight(1));
        builder.addEdge(_8, _7, new BasicEdgeWeight(1));
        builder.addEdge(_9, _8, new BasicEdgeWeight(1));
        builder.addEdge(_10, _0, new BasicEdgeWeight(2));
        builder.addEdge(_11, _0, new BasicEdgeWeight(3));

        graph = builder.build();
    }

    @Test
    public void testExecute() {

        GraphAlgorithm<Map<MyLocatableNode, Float>> bfsAlgorithm = GraphAlgorithms.createBFS(graph, _0, 2, 0);
        bfsAlgorithm.execute();
        Map<MyLocatableNode, Float> result = bfsAlgorithm.getResult();

        Assert.assertEquals(7, result.size());
        result.keySet().containsAll(Arrays.asList(_0, _1, _2, _4, _6, _7, _10));

        // Test again with a wider range
        bfsAlgorithm = GraphAlgorithms.createBFS(graph, _0, 3, 0);
        bfsAlgorithm.execute();
        result = bfsAlgorithm.getResult();

        Assert.assertEquals(11, result.size());
        result.keySet().containsAll(Arrays.asList(_0, _1, _2, _3, _4, _5, _6, _7, _8, _10, _11));
    }

}
