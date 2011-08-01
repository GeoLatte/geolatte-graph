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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 * No comment provided yet for this class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class InternalNodeWrapperTest {

    // Create this graph:
    //
    // dN1-----dN2-----dN3
    //  \              \
    //  `--------------'
    //

    private Object domainNode1 = new Object();
    private Object domainNode2 = new Object();
    private Object domainNode3 = new Object();

    private Object edgeLabel23 = new Object();
    private Object edgeLabel13 = new Object();

    private InternalNodeWrapper<Object, Object> iNode1;
    private InternalNodeWrapper<Object, Object> iNode2;
    private InternalNodeWrapper<Object, Object> iNode3;


    @Before
    public void setup() {

        iNode1 = new InternalNodeWrapper<Object, Object>(domainNode1);
        iNode2 = new InternalNodeWrapper<Object, Object>(domainNode2);
        iNode3 = new InternalNodeWrapper<Object, Object>(domainNode3);

        iNode1.addEdge(iNode2, new BasicEdgeWeight(1.0f));
        iNode2.addEdge(iNode3, new BasicEdgeWeight(2.0f), edgeLabel23);
        iNode1.addEdge(iNode3, new BasicEdgeWeight(3.0f), edgeLabel13);
    }


    @Test
    public void testGetWrappedNode() throws Exception {

        Assert.assertEquals(domainNode1, iNode1.getWrappedNode());
        Assert.assertEquals(domainNode2, iNode2.getWrappedNode());
        Assert.assertEquals(domainNode3, iNode3.getWrappedNode());
    }

    @Test
    public void testAddEdge() throws Exception {

        Object newDomainObject = new Object();
        InternalNodeWrapper<Object, Object> newInode = new InternalNodeWrapper<Object, Object>(newDomainObject);
        EdgeWeight edgeWeight = new BasicEdgeWeight(6.0f);

        iNode1.addEdge(newInode, edgeWeight);

        Assert.assertEquals(3, iNode1.getConnected().length);
        Assert.assertTrue(Arrays.asList(iNode1.getConnected()).contains(newInode));
        Assert.assertEquals(6.0f, iNode1.getWeightTo(newInode, 0), 0.005f);

        // Add another edge to the same node

        EdgeWeight anotherEdgeWeight = new BasicEdgeWeight(7.0f);
        iNode1.addEdge(newInode, anotherEdgeWeight);

        Assert.assertEquals(4, iNode1.getConnected().length);
        Assert.assertTrue(Arrays.asList(iNode1.getConnected()).contains(newInode));
    }

    @Test
    public void testGetWeightTo() throws Exception {

        Assert.assertEquals(1.0f, iNode1.getWeightTo(iNode2, 0), 0.005);
        Assert.assertEquals(2.0f, iNode2.getWeightTo(iNode3, 0), 0.005);
        Assert.assertEquals(3.0f, iNode1.getWeightTo(iNode3, 0), 0.005);

        Assert.assertEquals(Float.MAX_VALUE, iNode2.getWeightTo(iNode1, 0), 0.005);
    }

    @Test
    public void testLabelTo() throws Exception {

        Assert.assertEquals(null, iNode1.getLabelTo(iNode2));
        Assert.assertEquals(edgeLabel23, iNode2.getLabelTo(iNode3));
        Assert.assertEquals(edgeLabel13, iNode1.getLabelTo(iNode3));
    }

    @Test
    public void testAddReachableFrom() throws Exception {

    }


}
