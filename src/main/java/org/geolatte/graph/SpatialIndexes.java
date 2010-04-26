package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SpatialIndexes {

    //Factory methods.
    public static <E extends Nodal> SpatialIndexBuilder<E> createGridIndexBuilder(Envelope env, int resolution) {
        return new GridIndexBuilder<E>(env, resolution);
    }


    // Implementation Spatial Index Builders
    private static class GridIndexBuilder<T extends Nodal> implements SpatialIndexBuilder<T> {

        private final int resolution;
        private final Envelope env;

        //the resolution values satisfy the property:
        // value / resolution provides the cell number
        private final List<InternalNode<T>>[][] grid;
        private final int xNumCells, yNumCells;

        @SuppressWarnings("unchecked")
        private GridIndexBuilder(Envelope env, int resolution) {
            this.env = env;
            if (resolution < 1) {
                throw new IllegalArgumentException("Resolution must be larger than 1");
            }
            this.resolution = resolution;

            //number of cells
            this.xNumCells = (int) ((this.env.getMaxX() - this.env.getMinX()) + this.resolution) / this.resolution;
            this.yNumCells = (int) ((this.env.getMaxY() - this.env.getMinY()) + this.resolution) / this.resolution;

            this.grid = new List[this.xNumCells][this.yNumCells];

        }

        public boolean isWithinBounds(InternalNode<T> obj) {
            if (obj.getX() < this.env.getMinX() || obj.getX() > this.env.getMaxX()) {
                return false;
            }
            if (obj.getY() < this.env.getMinY() || obj.getY() > this.env.getMaxY()) {
                return false;
            }
            return true;
        }

        public void insert(InternalNode<T> obj) {
            if (!isWithinBounds(obj)) {
                throw new RuntimeException("Tried insert object that lies out of bounds: " + obj);
            }
            //calculate x/y cell index
            int xCellIdx = (int) (obj.getX() - this.env.getMinX()) / this.resolution;
            int yCellIdx = (int) (obj.getY() - this.env.getMinY()) / this.resolution;

            List<InternalNode<T>> cell = (List<InternalNode<T>>) this.grid[xCellIdx][yCellIdx];
            if (cell == null) {
                cell = new ArrayList<InternalNode<T>>();
                this.grid[xCellIdx][yCellIdx] = cell;

            }
            cell.add(obj);

        }

        public SpatialIndex<T> build() throws BuilderException {
            GridIndex<T> index = new GridIndex<T>(this.env, this.resolution);
            index.setGrid(toCompressedArray(this.grid));
            return index;
        }

        private Object[][][] toCompressedArray(List<InternalNode<T>>[][] grid) {
            Object[][][] newGrid = new Object[this.xNumCells][this.yNumCells][];
            for (int xi = 0; xi < grid.length; xi++) {
                List<InternalNode<T>>[] xar = grid[xi];
                for (int yi = 0; yi < xar.length; yi++) {
                    List<InternalNode<T>> cell = xar[yi];
                    if (cell != null) {
                        newGrid[xi][yi] = (Object[]) cell.toArray();
                    }
                }
            }
            return newGrid;
        }

    }

    // Implementation Spatial Index classes
    private static class GridIndex<T extends Nodal> implements SpatialIndex<T> {

        private final Envelope env;
        private final int resolution;
        private Object[][][] grid;

        private GridIndex(Envelope env, int resolution) {
            this.env = env;
            this.resolution = resolution;
        }

        private void setGrid(Object[][][] grid) {
            this.grid = grid;
        }

        private Object[] getCellContaining(Nodal obj) {
            if (obj == null) {
                throw null;
            }
            int ix = (int) (obj.getX() - this.env.getMinX()) / this.resolution;
            int iy = (int) (obj.getY() - this.env.getMinY()) / this.resolution;
            return grid[ix][iy];
        }

        @SuppressWarnings("unchecked")
        public boolean contains(InternalNode<T> obj) {
            if (obj == null) {
                return false;
            }
            Object[] cell = getCellContaining(obj);
            if (cell == null) {
                return false;
            }
            for (Object o : cell) {
                T c = (T) o;
                if (c.equals(obj)) {
                    return true;
                }
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        public List<InternalNode<T>> getNClosest(Nodal loc, int num, int maxDistance) {

            if (loc == null) {
                return new ArrayList<InternalNode<T>>();
            }
            int maxX = (int) Math.min(loc.getX() + maxDistance, this.env.getMaxX());
            int minX = (int) Math.max(loc.getX() - maxDistance, this.env.getMinX());
            int maxY = (int) Math.min(loc.getY() + maxDistance, this.env.getMaxY());
            int minY = (int) Math.max(loc.getY() - maxDistance, this.env.getMinY());

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
                        double dx = (double) (t.getX() - loc.getX());
                        double dy = (double) (t.getY() - loc.getY());
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

        public List<InternalNode<T>> query(Envelope window) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        public Iterator<InternalNode<T>> getObjects() {

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
                    return null;
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

        public List<InternalNode<T>> getObjectAt(Nodal loc) {
            List<InternalNode<T>> res = new ArrayList<InternalNode<T>>();
            if (loc == null) {
                return res;
            }
            Object[] cell = getCellContaining(loc);
            if (cell == null) {
                return res;
            }

            for (Object o : cell) {
                InternalNode<T> c = (InternalNode<T>) o;
                if (c.getX() == loc.getX()
                        && c.getY() == loc.getY()) {
                    res.add(c);
                }
            }
            return res;
        }

    }

}
