public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // Initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    // Ensure that lower and upper are within the bounds of the string
    if (lower > str.length()) {
        lower = str.length();
    }
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    if (lower > upper) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    // Find the first space after the lower index
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1) {
        result.append(str.substring(0, Math.min(upper, str.length())));
        // Append the suffix if the upper limit doesn't cover the whole string
        if (upper < str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > upper) {
        result.append(str.substring(0, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str.substring(0, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }
    return result.toString();
}
