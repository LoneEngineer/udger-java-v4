package org.udger.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class LRUCacheTest {

    private LRUCache<Integer, Integer> cache = new LRUCache<>(3);

    @Test
    public void testLRUCacheRespectCapacity() throws Exception {
        for(int i = 0; i < cache.getCapacity() + 1; i++){
            cache.put(i,i);
        }
        assertNull("Expected the first value put into cache to be absent after adding one more than capacity allows.", cache.get(0));
    }

    @Test
    public void testLRUCacheUpdatesLRUOrder() throws Exception {
        for(int i = 0; i < cache.getCapacity(); i++){
            cache.put(i,i);
        }
        assertNotNull("Expected to first put value to be in the cache.", cache.get(0));
        cache.put(cache.getCapacity()+1, cache.getCapacity()+1);
        assertNull("Expected the first value to have gone to the tail and second value to be removed for the next.", cache.get(1));
    }
}