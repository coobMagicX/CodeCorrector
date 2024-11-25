private static synchronized String getConvertedId(String id) {
    if (cZoneIdConversion == null) {
        // Backwards compatibility with TimeZone.
        Map<String, String> map = new HashMap<>();
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

        cZoneIdConversion = map;
    }
    if (cZoneIdConversion.containsKey(id)) {
        return cZoneIdConversion.get(id);
    } else {
        // Handle edge cases where input string does not match any known time zone ID
        Set<String> availableIDs = getAvailableIDs();
        for (String availableID : availableIDs) {
            if (availableID.contains(id)) {
                // Return the closest matching time zone ID
                return availableID;
            }
        }
    }

    // If no matching time zone ID is found, return a default value or throw an exception
    return "Unknown Time Zone";
}