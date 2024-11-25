private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null) {
        // Initialize the conversion map with known time zone IDs.
        map = new HashMap<String, String>();
        map.put("GMT", "UTC");
        map.put("MIT", "Pacific/Apia");
        map.put("HST", "Pacific/Honolulu");  // JDK 1.1 compatible
        map.put("AST", "America/Anchorage");
        map.put("PST", "America/Los_Angeles");
        map.put("MST", "America/Denver");  // JDK 1.1 compatible
        map.put("PNT", "America/Phoenix");
        map.put("CST", "America/Chicago");
        map.put("EST", "America/New_York");  // JDK 1.1 compatible
        map.put("IET", "America/Indianapolis");
        map.put("PRT", "America/Puerto_Rico");
        map.put("CNT", "America/St_Johns");
        map.put("AGT", "America/Buenos_Aires");
        map.put("BET", "America/Sao_Paulo");
        map.put("WET", "Europe/London");
        map.put("ECT", "Europe/Paris");
        map.put("ART", "Africa/Cairo");
        map.put("CAT", "Africa/Harare");
        map.put("EET", "Europe/Bucharest");
        map.put("EAT", "Africa/Addis_Ababa");
        map.put("MET", "Asia/Tehran");
        map.put("NET", "Asia/Yerevan");
        map.put("PLT", "Asia/Karachi");
        map.put("IST", "Asia/Calcutta");
        map.put("BST", "Asia/Dhaka");
        map.put("VST", "Asia/Saigon");
        map.put("CTT", "Asia/Shanghai");
        map.put("JST", "Asia/Tokyo");
        map.put("ACT", "Australia/Darwin");
        map.put("AET", "Australia/Sydney");
        map.put("SST", "Pacific/Guadalcanal");
        map.put("NST", "Pacific/Auckland");
        
        // Add additional mapping if needed based on the context.
        // For example, if there's a requirement to map an unknown ID:
        // map.put("UNKNOWN_ID", "FallbackTimeZone");

        cZoneIdConversion = map;
    }
    
    // Check if the provided ID exists in the map, and handle the case where it does not.
    String convertedId = map.get(id);
    if (convertedId == null) {
        throw new IllegalArgumentException("Unknown time zone ID: " + id);
    }

    return convertedId;
}