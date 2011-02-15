package org.geolatte.graph;

/**
 * @author Karel Maesen
 *         Copyright Geovise BVBA, 2010
 */
public interface EdgeLabel<M> {

    float getWeight(M modus);
}
