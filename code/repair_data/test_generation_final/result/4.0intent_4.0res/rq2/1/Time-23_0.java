private static final Object lock = new Object();
private static Map<String, String> cZoneIdConversion;

private static String getConvertedId(String id) {
    Map<String, String> localMap = cZoneIdConversion;
    if (localMap == null) {
        synchronized (lock) {
            localMap = cZoneIdConversion;
            if (localMap == null) {
                localMap = new HashMap<String, String>();
                localMap.put("GMT", "UTC");
                localMap.put("MIT", "Pacific/Apia");
                localMap.put("HST", "Pacific/Honolulu");
                localMap.put("AST", "America/Anchorage");
                localMap.put("PST", "America/Los_Angeles");
                localMap.put("MST", "America/Denver");
                localMap.put("PNT", "America/Phoenix");
                localMap.put("CST", "America/Chicago");
                localMap.put("EST", "America/New_York");
                localMap.put("IET", "America/Indianapolis");
                localMap.put("PRT", "America/Puerto_Rico");
                localMap.put("CNT", "America/St_Johns");
                localMap.put("AGT", "America/Buenos_Aires");
                localMap.put("BET", "America/Sao_Paulo");
                localMap.put("WET", "Europe/London");
                localMap.put("ECT", "Europe/Paris");
                localMap.put("ART", "Africa/Cairo");
                localMap.put("CAT", "Africa/Harare");
                localMap.put("EET", "Europe/Bucharest");
                localMap.put("EAT", "Africa/Addis_Ababa");
                localMap.put("MET", "Asia/Tehran");
                localMap.put("NET", "Asia/Yerevan");
                localMap.put("PLT", "Asia/Karachi");
                localMap.put("IST", "Asia/Calcutta");
                localMap.put("BST", "Asia/Dhaka");
                localMap.put("VST", "Asia/Saigon");
                localMap.put("CTT", "Asia/Shanghai");
                localMap.put("JST", "Asia/Tokyo");
                localMap.put("ACT", "Australia/Darwin");
                localMap.put("AET", "Australia/Sydney");
                localMap.put("SST", "Pacific/Guadalcanal");
                localMap.put("NST", "Pacific/Auckland");
                cZoneIdConversion = localMap;
            }
        }
    }
    return localMap.get(id);
}