public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // Initialize a variable to keep track of how much we have advanced the position
    int length = 0;

    // Get a substring that starts at 'position' and has a limited length to prevent infinite loop
    String str = text.substring(position);
    int strLength = str.length();

    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            // Check if the full timezone ID matches by ensuring the end of the substring is reached or it's followed by a non-letter character
            if ((str.length() == id.length()) || (!Character.isLetter(str.charAt(id.length())))) {
                bucket.setZone(DateTimeZone.forID(id));
                return position + id.length();
            }
        }
    }

    // If no match is found, make sure we move the position to indicate failure
    return ~position;
}