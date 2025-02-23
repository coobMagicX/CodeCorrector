public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    
    String remainingText = text.substring(position);
    String longestMatchingId = null;
    int longestMatchLength = 0;

