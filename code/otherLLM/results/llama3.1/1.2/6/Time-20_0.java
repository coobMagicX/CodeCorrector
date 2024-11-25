public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.equalsIgnoreCase(id)) { // Handle case sensitivity by using toLowerCase()
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}