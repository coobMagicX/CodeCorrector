private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null) {
        // Backwards compatibility with TimeZone.
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
        map.put("IET", "America/Indiana/Indianapolis");
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
    return map.get(id);
}

@Test
public void testForID_String_old() {
    Map<String, String> expectedMap = new HashMap<>();
    expectedMap.put("GMT", "UTC");
    expectedMap.put("MIT", "Pacific/Apia");
    expectedMap.put("HST", "Pacific/Honolulu");
    expectedMap.put("AST", "America/Anchorage");
    expectedMap.put("PST", "America/Los_Angeles");
    expectedMap.put("MST", "America/Denver");
    expectedMap.put("PNT", "America/Phoenix");
    expectedMap.put("CST", "America/Chicago");
    expectedMap.put("EST", "America/New_York");
    expectedMap.put("IET", "America/Indiana/Indianapolis");
    expectedMap.put("PRT", "America/Puerto_Rico");
    expectedMap.put("CNT", "America/St_Johns");
    expectedMap.put("AGT", "America/Buenos_Aires");
    expectedMap.put("BET", "America/Sao_Paulo");
    expectedMap.put("WET", "Europe/London");
    expectedMap.put("ECT", "Europe/Paris");
    expectedMap.put("ART", "Africa/Cairo");
    expectedMap.put("CAT", "Africa/Harare");
    expectedMap.put("EET", "Europe/Bucharest");
    expectedMap.put("EAT", "Africa/Addis_Ababa");
    expectedMap.put("MET", "Asia/Tehran");
    expectedMap.put("NET", "Asia/Yerevan");
    expectedMap.put("PLT", "Asia/Karachi");
    expectedMap.put("IST", "Asia/Calcutta");
    expectedMap.put("BST", "Asia/Dhaka");
    expectedMap.put("VST", "Asia/Saigon");
    expectedMap.put("CTT", "Asia/Shanghai");
    expectedMap.put("JST", "Asia/Tokyo");
    expectedMap.put("ACT", "Australia/Darwin");
    expectedMap.put("AET", "Australia/Sydney");
    expectedMap.put("SST", "Pacific/Guadalcanal");
    expectedMap.put("NST", "Pacific/Auckland");

    Map<String, String> actualMap = new HashMap<>();
    actualMap.put("GMT", getConvertedId("GMT"));
    actualMap.put("MIT", getConvertedId("MIT"));
    actualMap.put("HST", getConvertedId("HST"));
    actualMap.put("AST", getConvertedId("AST"));
    actualMap.put("PST", getConvertedId("PST"));
    actualMap.put("MST", getConvertedId("MST"));
    actualMap.put("PNT", getConvertedId("PNT"));
    actualMap.put("CST", getConvertedId("CST"));
    actualMap.put("EST", getConvertedId("EST"));
    actualMap.put("IET", getConvertedId("IET"));
    actualMap.put("PRT", getConvertedId("PRT"));
    actualMap.put("CNT", getConvertedId("CNT"));
    actualMap.put("AGT", getConvertedId("AGT"));
    actualMap.put("BET", getConvertedId("BET"));
    actualMap.put("WET", getConvertedId("WET"));
    actualMap.put("ECT", getConvertedId("ECT"));
    actualMap.put("ART", getConvertedId("ART"));
    actualMap.put("CAT", getConvertedId("CAT"));
    actualMap.put("EET", getConvertedId("EET"));
    actualMap.put("EAT", getConvertedId("EAT"));
    actualMap.put("MET", getConvertedId("MET"));
    actualMap.put("NET", getConvertedId("NET"));
    actualMap.put("PLT", getConvertedId("PLT"));
    actualMap.put("IST", getConvertedId("IST"));
    actualMap.put("BST", getConvertedId("BST"));
    actualMap.put("VST", getConvertedId("VST"));
    actualMap.put("CTT", getConvertedId("CTT"));
    actualMap.put("JST", getConvertedId("JST"));
    actualMap.put("ACT", getConvertedId("ACT"));
    actualMap.put("AET", getConvertedId("AET"));
    actualMap.put("SST", getConvertedId("SST"));
    actualMap.put("NST", getConvertedId("NST"));

    assertEquals(expectedMap, actualMap);
}