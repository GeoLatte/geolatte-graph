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

import org.geolatte.graph.InternalNode;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 * Test for the {@link BasicPredGraph} class.
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public class BasicPredGraphTest {

    @Test
    public void testConstructor() throws Exception {

        InternalNode<Object, Object> node = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);

        BasicPredGraph<Object, Object> basicPredGraph = new BasicPredGraph<Object, Object>(node, 42);

        Assert.assertEquals(node, basicPredGraph.getInternalNode());
        Assert.assertEquals(42, basicPredGraph.getWeight(), 0.005);
    }

    @Test
    public void testSetGetPredecessor() throws Exception {

        InternalNode<Object, Object> node1 = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);
        InternalNode<Object, Object> node2 = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);

        BasicPredGraph<Object, Object> basicPredGraph1 = new BasicPredGraph<Object, Object>(node1, 42);
        BasicPredGraph<Object, Object> basicPredGraph2 = new BasicPredGraph<Object, Object>(node2, 43);

        basicPredGraph1.setPredecessor(basicPredGraph2);

        Assert.assertEquals(basicPredGraph2, basicPredGraph1.getPredecessor());
    }

    @Test
    public void testGetInternalNode() throws Exception {

        InternalNode<Object, Object> node = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);

        BasicPredGraph<Object, Object> basicPredGraph = new BasicPredGraph<Object, Object>(node, 42);
        Assert.assertEquals(node, basicPredGraph.getInternalNode());
    }

    @Test
    public void testIterator() throws Exception {

        Object domainNode1 = new Object();
        Object domainNode2 = new Object();
        Object domainNode3 = new Object();

        InternalNode<Object, Object> node1 = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);
        Mockito.when(node1.getWrappedNode()).thenReturn(domainNode1);
        InternalNode<Object, Object> node2 = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);
        Mockito.when(node2.getWrappedNode()).thenReturn(domainNode2);
        InternalNode<Object, Object> node3 = (InternalNode<Object, Object>) Mockito.mock(InternalNode.class);
        Mockito.when(node3.getWrappedNode()).thenReturn(domainNode3);
        List<Object> domainNodes = Arrays.asList(domainNode1, domainNode2, domainNode3);

        BasicPredGraph<Object, Object> basicPredGraph1 = new BasicPredGraph<Object, Object>(node1, 42);
        BasicPredGraph<Object, Object> basicPredGraph2 = new BasicPredGraph<Object, Object>(node2, 43);
        BasicPredGraph<Object, Object> basicPredGraph3 = new BasicPredGraph<Object, Object>(node3, 44);

        basicPredGraph1.setPredecessor(basicPredGraph2).setPredecessor(basicPredGraph3);

        Iterator<InternalNode<Object, Object>> it = basicPredGraph1.iterator();
        int i = 0;
        for (; it.hasNext(); i++) {
            Object obj = it.next().getWrappedNode();
            Assert.assertEquals(domainNodes.get(i), obj);
        }

        Assert.assertEquals(i, domainNodes.size());

        try {
            it.next();
            Assert.fail("Expected exception not thrown.");
        } catch (NoSuchElementException e) {
            ; // do nothing
        }

        try {
            it.remove();
            Assert.fail("Expected exception not thrown.");
        } catch (UnsupportedOperationException e) {
            ; // do nothing
        }
    }

    @Test
    public void testHashCode() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }
}
