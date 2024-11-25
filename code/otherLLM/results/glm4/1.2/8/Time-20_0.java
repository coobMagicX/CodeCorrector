public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        // Ensure the zone ID is prefixed with "America/" or just the full ID if it's already correctly formatted
        if (str.startsWith(id) || (id.length() > 6 && str.startsWith("America/" + id))) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}