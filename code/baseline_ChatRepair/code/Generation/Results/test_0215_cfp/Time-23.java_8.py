private static synchronized String getConvertedId(String id) {
    if (cZoneIdConversion == null) {
        Map<String, String> map = new HashMap<>();
        map.put("GMT", "UTC");
        map.put("WET", "Europe/London");
        map.put("CET", "Europe/Paris");
        map.put("EET", "Europe/Athens");
        map.put("MET", "MET"); // Not converted, maintain original key, example purposes.
        // add any extra conversions
        cZoneIdConversion = map;
    }

    // Check for key in our conversion map, if not found, return the original ID.
    // This maintains backward compatibility as well as updating where necessary.
    return cZoneIdConversion.getOrDefault(id, id);
}
