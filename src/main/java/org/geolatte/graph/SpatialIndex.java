package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.Iterator;
import java.util.List;

public interface SpatialIndex <T extends Nodal> {
	
	public boolean contains(T obj);
	
	public List<T> query(Envelope env);
	
	public List<T> getNClosest(Nodal loc, int num, int maxDistance);
	
	public Iterator<T> getObjects();
	
	public List<T> getObjectAt(Nodal loc);

}
