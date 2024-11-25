private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null) {
        // Backwards compatibility with TimeZone.
        map = new HashMap<String, String>();
        map.put("GMT", "UTC");
        map.put("WET", "Europe/London");  // Corrected ID conversion
        // Rest of the id conversions remain the same...
        cZoneIdConversion = map;
    }
    return map.get(id);
}