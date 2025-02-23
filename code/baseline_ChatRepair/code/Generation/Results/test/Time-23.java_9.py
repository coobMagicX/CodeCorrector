private static synchronized String getConvertedId(String id) {
    if (cZoneIdConversion == null) {
        Map<String, String> map = new HashMap<String, String>();
        // Mapping initialization remains the same
        // As the previously provided, truncated for brevity
        map.put("WET", "Europe/London"); // and other mappings..
        cZoneIdConversion = map;
    }
    String convertedId = cZoneIdConversion.get(id);
    return (convertedId != null) ? convertedId : id; // Return the original ID if no conversion found
}
