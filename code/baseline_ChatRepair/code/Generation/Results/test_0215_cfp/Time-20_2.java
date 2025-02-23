public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    
    String longestMatch = null;
    int longestLength = 0;

    
    for (String id : ALL_IDS) {
        if