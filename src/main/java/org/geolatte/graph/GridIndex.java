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

import com.vividsolutions.jts.geom.Envelope;

import java.util.*;

/**
 * <p>
 * A spatial index based on a grid over the considered surface.
 * </p>
 *
 * @author Karel Maessen
 * @author Bert Vanhooff
 * @since SDK1.5
 */
class GridIndex<T extends Locatable> implements SpatialIndex<T> {

    private final Envelope env;
    private final int resolution;
    private Object[][][] grid;

    GridIndex(Envelope env, int resolution) {
        this.env = env;
        this.resolution = resolution;
    }

    public void setGrid(Object[][][] grid) {
        this.grid = grid;
    }

    private Object[] getCellContaining(Locatable obj) {
        if (obj == null) {
            throw null;
        }
        int ix = (int) (obj.getX() - this.env.getMinX()) / this.resolution;
        int iy = (int) (obj.getY() - this.env.getMinY()) / this.resolution;
        return grid[ix][iy];
    }

    @SuppressWarnings("unchecked")
    public boolean contains(InternalNode<T> internalNode) {
        if (internalNode == null) {
            return false;
        }
        Object[] cell = getCellContaining(internalNode.getWrappedNode());
        if (cell == null) {
            return false;
        }
        for (Object o : cell) {
            T c = (T) o;
            if (c.equals(internalNode)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<InternalNode<T>> getNClosest(Locatable locatable, int num, int maxDistance) {

        if (locatable == null) {
            return new ArrayList<InternalNode<T>>();
        }
        int maxX = (int) Math.min(locatable.getX() + maxDistance, this.env.getMaxX());
        int minX = (int) Math.max(locatable.getX() - maxDistance, this.env.getMinX());
        int maxY = (int) Math.min(locatable.getY() + maxDistance, this.env.getMaxY());
        int minY = (int) Math.max(locatable.getY() - maxDistance, this.env.getMinY());

        int minIdxX = (int) (minX - this.env.getMinX()) / this.resolution;
        int maxIdxX = (int) (maxX - this.env.getMinX()) / this.resolution;
        int minIdxY = (int) (minY - this.env.getMinY()) / this.resolution;
        int maxIdxY = (int) (maxY - this.env.getMinY()) / this.resolution;

        //define a utility class of labels
        class Label implements Comparable<Label> {
            private T obj;
            private long distance;

            private Label(T t, long dist) {
                this.obj = t;
                this.distance = dist;
            }

            public int compareTo(Label o) {
                return (int) (this.distance - o.distance);
            }

        }

        List<Label> candidates = new ArrayList<Label>();
        for (int ix = minIdxX; ix <= maxIdxX; ix++) {
            for (int iy = minIdxY; iy <= maxIdxY; iy++) {
                Object[] cell = this.grid[ix][iy];
                if (cell == null) continue;
                for (Object o : cell) {
                    T t = (T) o;
                    double dx = (double) (t.getX() - locatable.getX());
                    double dy = (double) (t.getY() - locatable.getY());
                    long distance = Math.round(Math.sqrt(dx * dx + dy * dy));
                    if (distance <= maxDistance) {
                        candidates.add(new Label(t, distance));
                    }
                }
            }
        }

        Collections.sort(candidates);
        List<InternalNode<T>> result = new ArrayList<InternalNode<T>>();
        for (int i = 0; i < Math.min(num, candidates.size()); i++) {
            result.add((InternalNode<T>) candidates.get(i).obj);
        }

        return result;
    }

    public List<InternalNode<T>> query(Envelope envelope) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Iterator<InternalNode<T>> getInternalNodes() {

        return new Iterator<InternalNode<T>>() {

            int ix = 0;
            int iy = 0;
            int i = 0;

            public boolean hasNext() {

                //first check if we have a non-null cell
                try {
                    while (grid[ix][iy] == null) {
                        incrementPointers();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // do nothing
                }
                return ix < grid.length;
            }

            public InternalNode<T> next() {

                if (hasNext()) {
                    InternalNode<T> retVal = (InternalNode<T>) grid[ix][iy][i];
                    incrementPointers();
                    return retVal;

                }

                throw new NoSuchElementException();
            }

            private void incrementPointers() {
                if (grid[ix][iy] == null) {
                    iy++;
                } else {
                    i++;
                    if (i > grid[ix][iy].length - 1) {
                        i = 0;
                        iy++;
                    }
                }
                if (iy > grid[ix].length - 1) {
                    iy = 0;
                    ix++;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported");

            }

        };
    }

    public List<InternalNode<T>> getNodeAt(Locatable loc) {
        List<InternalNode<T>> res = new ArrayList<InternalNode<T>>();
        if (loc == null) {
            return res;
        }
        Object[] cell = getCellContaining(loc);
        if (cell == null) {
            return res;
        }

        for (Object o : cell) {
            InternalNode<T> nd = (InternalNode<T>) o;
            Locatable c = nd.getWrappedNode();
            if (c.getX() == loc.getX()
                    && c.getY() == loc.getY()) {
                res.add(nd);
            }
        }
        return res;
    }

}
