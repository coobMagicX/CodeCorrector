public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0 || lower > str.length()) {
        return StringUtils.EMPTY;
    }

    // Handle the upper limit to ensure it does not exceed the string length
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    } else if (lower >= upper) { // Adjusted to handle case when lower is greater than or equal to upper
        return StringUtils.defaultString(appendToEnd); // If lower is not valid, append the end string only
    }

    StringBuffer result = new StringBuffer();

    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1 || index >= upper) {
        // No space found or space found at an invalid position, return abbreviated with appendToEnd
        result.append(str.substring(0, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        // Space found within the bounds of lower and upper
        result.append(str.substring(0, index));
        if (index < str.length() - 1) { // Check if there is a character after the space to append the end string
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }

    return result.toString();
}