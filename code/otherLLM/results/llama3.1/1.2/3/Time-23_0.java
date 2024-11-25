private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null || !map.containsKey(id)) { // Validate input ID to prevent NullPointerExceptions and non-existent IDs.
        // Handle non-existent IDs by returning a default value or error handling.
        return "Unknown"; // Default value for unknown IDs
    }
    return map.get(id);
}