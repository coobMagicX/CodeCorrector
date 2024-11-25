public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.toLowerCase().startsWith(id)) { // Case insensitive comparison
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length(); // Corrected position calculation
        }
    }
    return ~position;
}