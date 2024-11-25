private static synchronized String getConvertedId(String id) {
    Map<String, String> map = cZoneIdConversion;
    if (map == null) {
        // Backwards compatibility with TimeZone.
        map = new HashMap<String, String>();
        for (String jdk1_1_id : jdk1_1_ids) {
            DateTimeZone dtz = DateTimeZone.forTimeZone(TimeZone.getTimeZone(jdk1_1_id));
            map.put(jdk1_1_id, dtz.getID());
        }
        cZoneIdConversion = map;
    }
    return map.get(id);
}