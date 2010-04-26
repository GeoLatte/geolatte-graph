package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.Iterator;
import java.util.List;

public interface SpatialIndex<N extends Nodal> {

    public boolean contains(InternalNode<N> obj);

    public List<InternalNode<N>> query(Envelope env);

    public List<InternalNode<N>> getNClosest(Nodal loc, int num, int maxDistance);

    public Iterator<InternalNode<N>> getObjects();

    public List<InternalNode<N>> getObjectAt(Nodal loc);

}
