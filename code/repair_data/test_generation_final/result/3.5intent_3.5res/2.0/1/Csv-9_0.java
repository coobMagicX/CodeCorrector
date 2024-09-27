public Map<String, String> toMap() {
    if (mapping.isEmpty()) { // Check if the mapping is empty (no header)
        return new HashMap<String, String>(); // Return an empty map
    }
    return putIn(new HashMap<String, String>(values.length));
}