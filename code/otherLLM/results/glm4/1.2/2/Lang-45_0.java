public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0 || upper <= lower) {
        return str; // Return the original string if it's empty or upper is not greater than lower
    }

    // if the upper value exceeds the length of the input string, set to the length of the string
    if (upper > str.length()) {
        upper = str.length();
    }
    
    // If there are no spaces within the specified range, return the entire string without abbreviation or appending
    int spaceIndex = StringUtils.indexOf(str, " ", lower);
    if (spaceIndex == -1 || spaceIndex > upper) {
        return str; // No space found, or the first space is beyond the upper limit, return the whole string
    }

    StringBuffer result = new StringBuffer();
    result.append(str.substring(0, spaceIndex));
    
    // Only append the appendToEnd value if it's different from an empty string and within the specified range
    if (!appendToEnd.isEmpty()) {
        result.append(StringUtils.defaultString(appendToEnd));
    }
    return result.toString();
}