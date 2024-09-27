public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    if (text == null || position < 0 || position > text.length()) {
        return ~position; // Return failure if input is invalid
    }
    
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}