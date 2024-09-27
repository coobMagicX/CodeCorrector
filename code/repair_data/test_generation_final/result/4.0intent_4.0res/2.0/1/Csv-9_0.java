public Map<String, String> toMap() {
    if (mapping == null || mapping.isEmpty()) {
        return new HashMap<String, String>();
    }
    return putIn(new HashMap<String, String>(values.length));
}