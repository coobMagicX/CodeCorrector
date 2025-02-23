public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String remainingText = text.substring(position);
    
    // Initialize variables to track the longest matching time zone ID
    String bestMatch = null;
    int bestMatchLength = 0;

    // Iterate over all known IDs to find the longest matching prefix from the position
    for (String id : ALL_IDS) {
        if (remainingText.startsWith(id) && id.length() > bestMatchLength) {
            bestMatch = id;
            bestMatchLength = id.length();
        }
    }

    // If a valid timezone ID was found that matches longest from current position
    if (bestMatch != null) {
        bucket.setZone(DateTimeZone.forID(bestMatch));
        return position + bestMatchLength;
    }

    // No matching time zone found, return the negative position to signal failure
    return ~position;
}
