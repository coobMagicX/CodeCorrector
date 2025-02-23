public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String remainingText = text.substring(position);

    
    int maxLength = 0;
    String matchingZoneId = null;

    
    for (String id : ALL_IDS) {
        if (remainingText.startsWith(id)