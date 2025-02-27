private static synchronized String getConvertedId(String id) {
    if (cZoneIdConversion == null) {
        initializeConversionMap();
    }

    String convertedId = cZoneIdConversion.get(id);
    // Return the converted ID if found, else return the original ID
    return (convertedId != null) ? convertedId : id;
}

private static void initializeConversionMap() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("GMT", "UTC");
    map.put("MIT", "Pacific/Apia");
    map.put("HST", "Pacific/Honolulu");
    map.put("AST", "America/Anchorage");
    map.put("PST", "America/Los_Angeles");
    map.put("MST", "America/Denver");
    map.put("PNT", "America/Phoenix");
    map.put("CST", "America/Chicago");
    map.put("EST", "America/New_York");
    map.put("IET", "America/Indianapolis");
    map.put("PRT", "America/Puerto_Rico");
    map.put("CNT", "America/St_Johns");
    map.put("AGT", "America/Buenos_Aires");
    map.put("BET", "America/Sao_Paulo");
    map.put("WET", "Europe/London");
    map.put("MET", "Europe/Berlin");  // Corrected to match expectations
    map.put("ECT", "Europe/Paris");
    map.put("ART", "Africa/Cairo");
    map.put("CAT", "Africa/Harare");
    map.put("EET", "Europe/Bucharest");
    map.put("EAT", "Africa/Addis_Ababa");
    map.put("NET", "Asia/Yerevan");
    map.put("PLT", "Asia/Karachi");
    map.put("IST", "Asia/Kolkata");  // Corrected from India/Calcutta
    map.put("BST", "Asia/Dhaka");
    map.put("VST", "Asia/Ho_Chi_Minh"); // Corrected from Saigon
    map.put("CTT", "Asia/Shanghai");
    map.put("JST", "Asia/Tokyo");
    map.put("ACT", "Australia/Darwin");
    map.put("AET", "Australia/Sydney");
    map.put("SST", "Pacific/Guadalcanal");
    map.put("NST", "Pacific/Auckland");
    cZoneIdConversion = map;
}
