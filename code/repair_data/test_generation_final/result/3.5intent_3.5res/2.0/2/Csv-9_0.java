        // Method 1: isEmpty()
        // Returns true if the mapping is empty
        boolean isEmpty();
        
        // Method 2: size()
        // Returns the number of entries in the mapping
        int size();
        
        // Method 3: containsKey(Object key)
        // Returns true if the mapping contains a mapping for the specified key
        boolean containsKey(Object key);
        
        // Method 4: put(K key, V value)
        // Associates the specified value with the specified key in the mapping
        V put(K key, V value);
        
        // Method 5: remove(Object key)
        // Removes the mapping for the specified key from the mapping, if present
        V remove(Object key);
        
        // Method 6: keySet()
        // Returns a set view of the keys contained in the mapping
        Set<K> keySet();
        
        // Method 7: entrySet()
        // Returns a set view of the mappings contained in the mapping
        Set<Map.Entry<K, V>> entrySet();
        
        // Method 8: clear()
        // Removes all mappings from the mapping
        void clear();