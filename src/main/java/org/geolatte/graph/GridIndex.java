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

import java.util.*;

/**
 * <p>
 * A spatial index based on a grid over the considered area (extent), edges included.
 * </p>
 *
 * @author Karel Maessen
 * @author Bert Vanhooff
 * @since SDK1.5
 */
class GridIndex<T extends Locatable> implements SpatialIndex<T> {

    private final Extent extent;
    private final float resolution;
    /*
     * The actual grid, elements are always of type T
     * grid[x][y][elements]
     *
     *      x
     *    y ----------------
     *      | [] | [] | [] |
     *      ----------------
     *      | [] | [] | [] |
     *      ----------------
     *      | [] | [] | [] |
     *      ----------------
     */
    private Object[][][] grid;

    GridIndex(Extent extent, float resolution) {
        this.extent = extent;
        this.resolution = resolution;
    }

    void setGrid(Object[][][] grid) {
        this.grid = grid;
    }

    private Object[] getCellContaining(Locatable obj) {
        if (obj == null) {
            throw null;
        }
        int ix = (int) ((obj.getX() - this.extent.getMinX()) / this.resolution);
        int iy = (int) ((obj.getY() - this.extent.getMinY()) / this.resolution);
        return grid[ix][iy];
    }

    @SuppressWarnings("unchecked")
    public boolean contains(T node) {
        if (node == null) {
            return false;
        }
        Object[] cell = getCellContaining(node);
        if (cell == null) {
            return false;
        }
        for (Object o : cell) {
            T c = (T) o;
            if (c.equals(node)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<T> getNClosest(Locatable locatable, int num, float maxDistance) {

        if (locatable == null || num == 0) {
            return new ArrayList<T>();
        }

        // Real min and max values
        double maxX = Math.min(locatable.getX() + maxDistance, this.extent.getMaxX());
        double minX = Math.max(locatable.getX() - maxDistance, this.extent.getMinX());
        double maxY = Math.min(locatable.getY() + maxDistance, this.extent.getMaxY());
        double minY = Math.max(locatable.getY() - maxDistance, this.extent.getMinY());

        // Convert values to grid indexes
        int minIdxX = (int) ((minX - this.extent.getMinX()) / this.resolution);
        int maxIdxX = (int) ((maxX - this.extent.getMinX()) / this.resolution);
        int minIdxY = (int) ((minY - this.extent.getMinY()) / this.resolution);
        int maxIdxY = (int) ((maxY - this.extent.getMinY()) / this.resolution);

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
        List<T> result = new ArrayList<T>(Math.min(num, candidates.size()));
        for (int i = 0; i < Math.min(num, candidates.size()); i++) {
            result.add((T)candidates.get(i).obj); // TODO : not sure why this cast is required (does not compile without)
        }

        return result;
    }

    public List<T> query(Extent extent) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Iterator<T> getNodes() {

        return new Iterator<T>() {

            int ix = 0;
            int iy = 0;
            int i = 0;

            public boolean hasNext() {

                //getFirst check if we have a non-null cell
                try {
                    while (grid[ix][iy] == null) {
                        incrementPointers();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // do nothing
                }
                return ix < grid.length;
            }

            public T next() {

                if (hasNext()) {
                    T retVal = (T) grid[ix][iy][i];
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

    public List<T> getNodeAt(Locatable loc) {
        List<T> res = new ArrayList<T>();
        if (loc == null) {
            return res;
        }
        Object[] cell = getCellContaining(loc);
        if (cell == null) {
            return res;
        }

        for (Object o : cell) {
            T nd = (T) o;
            if (nd.getX() == loc.getX()
                    && nd.getY() == loc.getY()) {
                res.add(nd);
            }
        }
        return res;
    }

}
