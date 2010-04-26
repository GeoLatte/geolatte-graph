package org.geolatte.data;

public interface Visitor<K> {

    public void visit(K obj);

}
