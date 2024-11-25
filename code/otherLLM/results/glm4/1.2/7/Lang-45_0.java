public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    // Set the upper limit to be within the bounds of the string length
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    // Ensure that 'upper' is not less than 'lower'
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    int index = str.indexOf(" ", lower);
    
    if (index == -1) { // No space found, use the entire string
        result.append(str.substring(0, upper));
        if (upper != str.length()) { // Check for abbreviation
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > upper) { // Space is after the upper limit
        result.append(str.substring(0, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else { // Space is within the lower and upper bounds
        result.append(str.substring(0, index));
        if (upper != index) { // Check for abbreviation
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }
    return result.toString();
}