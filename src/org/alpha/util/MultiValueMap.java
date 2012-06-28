package org.alpha.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MultiValueMap<K, V> {

    private final Map<K, Collection<V>> map = new HashMap<K, Collection<V>>();


    public void put(K key, V value) {
        Collection<V> existingValues = map.get(key);
        if (existingValues == null) {
            existingValues = new ArrayList<V>();
            map.put(key, existingValues);
        }
        existingValues.add(value);
    }

    public Collection<V> get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

}
