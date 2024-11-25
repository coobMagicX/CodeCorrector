public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.toLowerCase().startsWith(id)) { // handle case sensitivity by converting input to lowercase
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        } else if (str.startsWith(id.toUpperCase()) || str.startsWith(id.toLowerCase())) { // consider longer IDs by checking for uppercase and lowercase matches
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    if (position > 0 && position <= text.length() - 1) { // validate input to prevent out-of-bounds errors
        return ~position;
    } else {
        throw new IllegalArgumentException("Invalid input");
    }
}