public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0 || lower < 0) {
        return StringUtils.EMPTY; // Added check for lower being negative
    }

    // Adjusting the upper boundary to not exceed the string's length
    upper = Math.min(upper, str.length());

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    
    if (index == -1) {
        result.append(str.substring(lower, upper));
        // only if abbreviation has occurred do we append the appendToEnd value
        if (upper < str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > upper) {
        result.append(str.substring(lower, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str.substring(lower, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }
    
    return result.toString();
}