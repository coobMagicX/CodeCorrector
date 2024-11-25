public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    // Improved logic to handle shorter time zone IDs correctly
    String normalizedStr = normalizeTimeZoneId(str);
    
    for (String id : ALL_IDS) {
        if (normalizedStr.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}

private String normalizeTimeZoneId(String timeZoneId) {
    // Check if the timeZoneId is already normalized
    for (String knownId : ALL_IDS) {
        if (knownId.equals(timeZoneId)) {
            return timeZoneId;
        }
    }

    // If not, try to find and append the full ID from ALL_IDS where the given ID is a substring
    for (String knownId : ALL_IDS) {
        if (timeZoneId.startsWith(knownId)) {
            String fullId = knownId + (knownId.endsWith("/") ? "" : "/") + timeZoneId.substring(knownId.length());
            return fullId;
        }
    }

    // If no match is found, return the original ID as a fallback
    return timeZoneId;
}