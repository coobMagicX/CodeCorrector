private static Map<String, String> cZoneIdConversion = null;
private static Map<String, String> cOldZoneIdMapping = null;

private static synchronized void initializeMaps() {
    Map<String, String> newMap = new HashMap<String, String>();
    Map<String, String> oldMap = new HashMap<String, String>();

    newMap.put("GMT", "UTC");
    newMap.put("MIT", "Pacific/Apia");
    newMap.put("HST", "Pacific/Honolulu");
    newMap.put("AST", "America/Anchorage");
    newMap.put("PST", "America/Los_Angeles");
    newMap.put("MST", "America/Denver");
    newMap.put("PNT", "America/Phoenix");
    newMap.put("CST", "America/Chicago");
    newMap.put("EST", "America/New_York");
    newMap.put("IET", "America/Indianapolis");
    newMap.put("PRT", "America/Puerto_Rico");
    newMap.put("CNT", "America/St_Johns");
    newMap.put("AGT", "America/Buenos_Aires");
    newMap.put("BET", "America/Sao_Paulo");
    newMap.put("WET", "Europe/London");
    newMap.put("ECT", "Europe/Paris");
    newMap.put("ART", "Africa/Cairo");
    newMap.put("CAT", "Africa/Harare");
    newMap.put("EET", "Europe/Bucharest");
    newMap.put("EAT", "Africa/Addis_Ababa");
    newMap.put("MET", "Asia/Tehran");
    newMap.put("NET", "Asia/Yerevan");
    newMap.put("PLT", "Asia/Karachi");
    newMap.put("IST", "Asia/Calcutta");
    newMap.put("BST", "Asia/Dhaka");
    newMap.put("VST", "Asia/Saigon");
    newMap.put("CTT", "Asia/Shanghai");
    newMap.put("JST", "Asia/Tokyo");
    newMap.put("ACT", "Australia/Darwin");
    newMap.put("AET", "Australia/Sydney");
    newMap.put("SST", "Pacific/Guadalcanal");
    newMap.put("NST", "Pacific/Auckland");

    // Map old IDs to themselves for original usage.
    oldMap.putAll(newMap);
    oldMap.keySet().forEach(key -> oldMap.put(key, key));

    cZoneIdConversion = newMap;
    cOldZoneIdMapping = oldMap;
}

private static synchronized String getConvertedId(String id, boolean returnOld) {
    if (cZoneIdConversion == null || cOldZoneIdMapping == null) {
        initializeMaps();
    }
    if (returnOld) {
        return cOldZoneIdMapping.getOrDefault(id, id);
    } else {
        return cZoneIdConversion.getOrDefault(id, id);
    }
}
