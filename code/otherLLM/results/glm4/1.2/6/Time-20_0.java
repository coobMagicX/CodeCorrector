public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }

    // Check if the remaining substring does not contain any of the IDs
    String remainingStr = str;
    for (String id : ALL_IDS) {
        if (remainingStr.contains(id)) {
            // If any ID is found in the remaining substring, it means that the previous check did not find a match,
            // and we should try again to parse using the new time zone ID.
            bucket.setZone(DateTimeZone.forID(id));
            return position + remainingStr.indexOf(id);
        }
    }

    return ~position; // Return negative value to indicate failure
}