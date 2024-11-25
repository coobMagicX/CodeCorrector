public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.contains(id)) { // Changed from startsWith to contains
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    appendUnknownString(new StringBuffer(), estimatePrintedLength()); // Added validation and unknown string handling
    return ~position;
}