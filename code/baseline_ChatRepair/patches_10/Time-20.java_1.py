public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // Extract the relevant part for time zone matching from the given position in the text
    String remainingText = text.substring(position);
    
    // Keep track of the longest matching time zone ID
    String longestMatch = null;
    int maxLength = 0;

    // Iterate over all possible time zone IDs to find the longest matching one from the current position
    for (String id : ALL_IDS) {
        if (remainingText.startsWith(id) && id.length() > maxLength) {
            longestMatch = id;
            maxLength = id.length();
        }
    }

    // If a matching time zone ID is found then set the time zone in the bucket and return the new position
    if (longestMatch != null) {
        bucket.setZone(DateTimeZone.forID(longestMatch));
        return position + longestMatch.length();
    }
    
    // If no
    return ~position;            
}            