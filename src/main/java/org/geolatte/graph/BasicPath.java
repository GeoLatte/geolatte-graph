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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicPath<N> implements Path<N> {

    private List<N> nodes = new LinkedList<N>();

    private boolean valid = false;

    private float weight;

    public void insert(N nd) {
        nodes.add(0, nd);
    }

    public void setValid(boolean v) {
        this.valid = v;
    }

    public void setTotalWeight(float w) {
        this.weight = w;
    }


    public N getDestination() {
        return this.nodes.get(this.nodes.size() - 1);
    }

    public N getSource() {
        return this.nodes.get(0);
    }

    public boolean isValid() {
        return this.valid;
    }


    public float totalWeight() {

        return this.weight;
    }

    public Iterator<N> iterator() {
        return this.nodes.iterator();
    }

    public String toString() {
        StringBuilder stBuf = new StringBuilder();
        stBuf.append("Nodes: ");
        for (N nd : this) {
            stBuf.append(nd)
                    .append("\n");
        }
        return stBuf.toString();
    }

}
