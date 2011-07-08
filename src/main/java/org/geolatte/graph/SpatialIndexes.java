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
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.graph;

import com.vividsolutions.jts.geom.Envelope;

import java.util.ArrayList;
import java.util.List;

public class SpatialIndexes {

    //Factory methods.
    public static <E extends Locatable> SpatialIndexBuilder<E> createGridIndexBuilder(Envelope env, int resolution) {
        return new GridIndexBuilder<E>(env, resolution);
    }


    // Implementation Spatial Index Builders
    private static class GridIndexBuilder<T extends Locatable> implements SpatialIndexBuilder<T> {

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

        public boolean isWithinBounds(InternalNode<T> nd) {
            T obj = nd.getWrappedNodal();
            if (obj.getX() < this.env.getMinX() || obj.getX() > this.env.getMaxX()) {
                return false;
            }
            if (obj.getY() < this.env.getMinY() || obj.getY() > this.env.getMaxY()) {
                return false;
            }
            return true;
        }

        public void insert(InternalNode<T> nd) {
            if (!isWithinBounds(nd)) {
                throw new RuntimeException("Tried insert object that lies out of bounds: " + nd);
            }

            T obj = nd.getWrappedNodal();

            //calculate x/y cell index
            int xCellIdx = (int) (obj.getX() - this.env.getMinX()) / this.resolution;
            int yCellIdx = (int) (obj.getY() - this.env.getMinY()) / this.resolution;

            List<InternalNode<T>> cell = (List<InternalNode<T>>) this.grid[xCellIdx][yCellIdx];
            if (cell == null) {
                cell = new ArrayList<InternalNode<T>>();
                this.grid[xCellIdx][yCellIdx] = cell;

            }
            cell.add(nd);

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



}
