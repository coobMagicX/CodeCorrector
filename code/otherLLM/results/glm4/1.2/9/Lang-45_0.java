public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null || str.isEmpty()) {
        return StringUtils.EMPTY;
    }

    // Ensure the bounds are valid
    lower = Math.max(0, lower);
    upper = Math.min(str.length(), upper);

    StringBuffer result = new StringBuffer();
    int index = -1;

    // Check for a space within the specified bounds
    if (upper > lower) {
        index = StringUtils.indexOf(str, " ", lower);
    }

    // Use the correct substring based on the found index and bounds
    if (index == -1 || upper >= str.length()) {
        // No space or we are at the end of the string, append the whole string
        result.append(str.substring(0, Math.min(upper, str.length())));
        if (upper < str.length()) { // Only append if abbreviation has occurred
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else {
        // There is a space within the bounds, but it might be at or beyond the upper limit
        if (index > upper) {
            result.append(str.substring(0, upper));
            result.append(StringUtils.defaultString(appendToEnd));
        } else {
            result.append(str.substring(0, index));
            if (upper < str.length()) { // Only append if abbreviation has occurred
                result.append(StringUtils.defaultString(appendToEnd));
            }
        }
    }

    return result.toString();
}