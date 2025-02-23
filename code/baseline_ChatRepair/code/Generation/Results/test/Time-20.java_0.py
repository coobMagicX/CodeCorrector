public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // Extract the relevant part for timezone matching from the given position in the text
    String remainingText = text.substring(position);

    // Track the matched zone id with the longest length
    String matchedZoneId = null;
    int matchedLength = 0;

    // Iterate over all possible timezone IDs to find the longest matching ID
    for (String id : ALL_IDS) {
        if (remainingText.startsWith(id) && id.length() > matchedLength) {
            // Update the matched zone id and its length
            matchedZoneId = id;
            matchedLength = id.length();
        }
    }

    // If a matching zone ID was found set the timezone in the bucket and return new position
    if (matchedZoneId != null) {
        bucket.setZone(DateTimeZone.forID(matchedZoneId));
