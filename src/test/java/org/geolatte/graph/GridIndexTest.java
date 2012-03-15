package org.geolatte.graph;

import org.geolatte.stubs.MyLocatable;
import org.geolatte.stubs.MyLocatableNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * Unit test for the internal {@link GridIndex} class. We use the {@link SpatialIndexes} factory to create a GridIndex
 * since that is the only method in the public API to create a GridIndex.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class GridIndexTest {

    /**
     * We use a 200*200 grid with cells of 50.
     * We insert these points:
     *
     *
     *     0      50      100     150     200
     *   0 ---------------------------------..
     *     | n1,n2 |       |       |       |
     *  50 ---------------------------------
     *     |       |  n5   |       |       |n3
     * 100 ---------------------------------..
     *     |       |       | n6,n7 |       |
     * 150 ---------------------------------..
     *     |       |       |       |       |
     * 200 ---------------------------------..
     *     '       '       '       '       'n4
     */

    private MyLocatableNode n1 = new MyLocatableNode(1, 0, 0);
    private MyLocatableNode n2 = new MyLocatableNode(2, 5, 5);
    private MyLocatableNode n3 = new MyLocatableNode(3, 55, 200);
    private MyLocatableNode n4 = new MyLocatableNode(4, 200, 200);
    private MyLocatableNode n5 = new MyLocatableNode(5, 56, 60);
    private MyLocatableNode n6 = new MyLocatableNode(6, 130, 130);
    private MyLocatableNode n7 = new MyLocatableNode(7, 130, 130);

    private SpatialIndex<Locatable> index;

    @Before
    public void setup() throws Exception {

        SpatialIndexBuilder<Locatable> builder = SpatialIndexes.createGridIndexBuilder(new Extent(0, 0, 200, 200), 50);
        builder.insert(n1);
        builder.insert(n2);
        builder.insert(n3);
        builder.insert(n4);
        builder.insert(n5);
        builder.insert(n6);
        builder.insert(n7);

        index = builder.build();
    }

    @Test
    public void testContains() throws Exception {

        Assert.assertTrue(index.contains(n1));
        Assert.assertTrue(index.contains(n2));
        Assert.assertTrue(index.contains(n3));
        Assert.assertTrue(index.contains(n4));
        Assert.assertTrue(index.contains(n5));
        Assert.assertTrue(index.contains(n6));
        Assert.assertTrue(index.contains(n7));
    }

    @Test
    public void testGetNClosest() throws Exception {

        List<Locatable> closest;

        // Check point count starting from (0 0)
        closest = index.getNClosest(new MyLocatable(0, 0), 10, 0);
        Assert.assertEquals(1, closest.size());
        closest = index.getNClosest(new MyLocatable(0, 0), 10, 6);
        Assert.assertEquals(1, closest.size());
        closest = index.getNClosest(new MyLocatable(0, 0), 10, 7); // includes points on (5 5)
        Assert.assertEquals(2, closest.size());
        closest = index.getNClosest(new MyLocatable(0, 0), 10, 80);
        Assert.assertEquals(2, closest.size());
        closest = index.getNClosest(new MyLocatable(0, 0), 10, 84);
        Assert.assertEquals(3, closest.size());

        closest = index.getNClosest(new MyLocatable(0, 0), 3, 200*200);
        Assert.assertEquals(3, closest.size());

        // Check sorting
        closest = index.getNClosest(new MyLocatable(0, 0), 100, 200*200);
        Assert.assertEquals(7, closest.size());
        Assert.assertEquals(n1, closest.get(0));
        Assert.assertEquals(n2, closest.get(1));
        Assert.assertEquals(n5, closest.get(2));
        Assert.assertTrue(closest.subList(3, 5).contains(n6)); // order of equal point does not matter
        Assert.assertTrue(closest.subList(3, 5).contains(n7));
        Assert.assertEquals(n3, closest.get(5));
        Assert.assertEquals(n4, closest.get(6));

        // distance n6/n5 = ~101.8
        // distance n6/n4 = ~98.99
        // distance n6/n3 = ~102.6

        closest = index.getNClosest(n6  , 100, 10);
        Assert.assertEquals(2, closest.size());
        closest = index.getNClosest(n6  , 100, 100);
        Assert.assertEquals(3, closest.size());
        closest = index.getNClosest(n6  , 100, 102);
        Assert.assertEquals(4, closest.size());
        closest = index.getNClosest(n6  , 100, 104);
        Assert.assertEquals(5, closest.size());
    }

    @Test
    public void testGetNodeAt() throws Exception {

        Assert.assertEquals(1, index.getNodeAt(n1).size());
        Assert.assertEquals(n1, index.getNodeAt(n1).get(0));
        Assert.assertEquals(1, index.getNodeAt(n4).size());
        Assert.assertEquals(n4, index.getNodeAt(n4).get(0));
        Assert.assertEquals(2, index.getNodeAt(n6).size());
        Assert.assertTrue(index.getNodeAt(n6).contains(n6));
        Assert.assertTrue(index.getNodeAt(n6).contains(n7));

        Assert.assertEquals(0, index.getNodeAt(new MyLocatable(34, 43)).size());
        Assert.assertEquals(0, index.getNodeAt(null).size());
    }


}
