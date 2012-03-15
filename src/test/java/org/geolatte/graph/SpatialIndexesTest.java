package org.geolatte.graph;

import org.geolatte.stubs.MyLocatable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * <p>
 * Unit test for the {@link SpatialIndexes} class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class SpatialIndexesTest {

    /**
     * Verifies that the gridindexbuilder adds nodes to the gridindex correctly.
     * @throws Exception
     */
    @Test
    public void testCreateGridIndexBuilder() throws Exception {

        SpatialIndexBuilder<MyLocatable> builder = SpatialIndexes.createGridIndexBuilder(new Extent(0, 0, 200, 200), 50);

        MyLocatable a = new MyLocatable(0, 0);
        builder.insert(a);
        MyLocatable b = new MyLocatable(200, 200);
        builder.insert(b);
        MyLocatable c= new MyLocatable(100, 100);
        builder.insert(c);
        MyLocatable d = new MyLocatable(53, 96);
        builder.insert(d);

        SpatialIndex<MyLocatable> index = builder.build();

        Assert.assertTrue(index.contains(a));
        Assert.assertTrue(index.contains(b));
        Assert.assertTrue(index.contains(c));
        Assert.assertTrue(index.contains(d));

        int count = 0;
        Iterator<MyLocatable> it = index.getNodes();
        while (it.hasNext()) {it.next(); count++;}
        Assert.assertEquals(4, count);

    }
}
