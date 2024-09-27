public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    for (String id : ALL_IDS) {
        if (text.regionMatches(true, position, id, 0, id.length())) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}