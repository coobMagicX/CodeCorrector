public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String remainingText = text.substring(position);

    
    int longestMatchIndex = -1;
    int longestMatchLength = 0;

    
    for (int i = 0; i < ALL