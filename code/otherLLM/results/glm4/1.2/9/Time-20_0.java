public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        // Check if the substring starts with the zone ID and handle similar or overlapping IDs
        if (str.startsWith(id)) {
            try {
                DateTimeZone zone = DateTimeZone.forID(id);
                bucket.setZone(zone);
                return position + id.length();
            } catch (DateTimeException e) {
                // Log or handle the exception, but continue checking other IDs to avoid incorrect zone settings
                continue;
            }
        }
    }
    return ~position; // Return negative value if no matching ID is found
}