public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // Initialize a variable to store the longest matching ID found
    String longestMatch = null;

    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            // If longestMatch is null or current ID is longer than the longest ID found so far
            if (longestMatch == null || id.length() > longestMatch.length()) {
                bucket.setZone(DateTimeZone.forID(id));
                longestMatch = id; // Update the longest matching ID
            }
        }
    }

    // If a matching timezone ID has been found, return the position of the start of the ID and its length
    if (longestMatch != null) {
        return position + longestMatch.length();
    } else {
        // Return an invalid position if no match is found
        return ~position;
    }
}