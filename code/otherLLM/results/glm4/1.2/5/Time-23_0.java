private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null) {
        // Initialize the map with backwards compatibility entries for TimeZone.
        map = new HashMap<String, String>() {{
            put("GMT", "UTC");
            put("MIT", "Pacific/Apia");
            put("HST", "Pacific/Honolulu");  // JDK 1.1 compatible
            put("AST", "America/Anchorage");
            put("PST", "America/Los_Angeles");
            put("MST", "America/Denver");  // JDK 1.1 compatible
            put("PNT", "America/Phoenix");
            put("CST", "America/Chicago");
            put("EST", "America/New_York");  // JDK 1.1 compatible
            put("IET", "America/Indianapolis");
            put("PRT", "America/Puerto_Rico");
            put("CNT", "America/St_Johns");
            put("AGT", "America/Buenos_Aires");
            put("BET", "America/Sao_Paulo");
            put("WET", "Europe/London");
            put("ECT", "Europe/Paris");
            put("ART", "Africa/Cairo");
            put("CAT", "Africa/Harare");
            put("EET", "Europe/Bucharest");
            put("EAT", "Africa/Addis_Ababa");
            put("MET", "Asia/Tehran");
            put("NET", "Asia/Yerevan");
            put("PLT", "Asia/Karachi");
            put("IST", "Asia/Calcutta");
            put("BST", "Asia/Dhaka");
            put("VST", "Asia/Saigon");
            put("CTT", "Asia/Shanghai");
            put("JST", "Asia/Tokyo");
            put("ACT", "Australia/Darwin");
            put("AET", "Australia/Sydney");
            put("SST", "Pacific/Guadalcanal");
            put("NST", "Pacific/Auckland");
        }};
        cZoneIdConversion = map;
    }
    return map.get(id);
}