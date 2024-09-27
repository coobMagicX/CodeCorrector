public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    String longestMatch = "";
    for (String id : ALL_IDS) {
        if (str.startsWith(id) && id.length() > longestMatch.length()) {
            longestMatch = id;
        }
    }
    if (!longestMatch.isEmpty()) {
        bucket.setZone(DateTimeZone.forID(longestMatch));
        return position + longestMatch.length();
    }
    return ~position;
}