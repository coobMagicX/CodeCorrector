public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    String longestMatch = null;
    for (String id : ALL_IDS) {
        if (str.startsWith(id) && (longestMatch == null || id.length() > longestMatch.length())) {
            longestMatch = id;
        }
    }
    if (longestMatch != null) {
        bucket.setZone(DateTimeZone.forID(longestMatch));
        return position + longestMatch.length();
    }
    return ~position;
}