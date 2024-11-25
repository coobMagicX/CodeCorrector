public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    // if the upper value is -1 (i.e. no limit) or is greater
    // than the length of the string, set to the length of the string
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    // if the lower value is greater than the length of the string,
    // raise it to the length of the string as well
    if (lower > str.length()) {
        lower = str.length();
    }

    // if upper is less than lower, raise it to lower
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1 || index > upper) {
        // If no space found in the string or the index is beyond the upper limit,
        // append up to the upper limit.
        result.append(str.substring(0, upper));
        // only if abbreviation has occurred do we append the appendToEnd value
        if (upper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else {
        // If a space is found within the bounds of lower and upper,
        // append up to the index of the first space.
        result.append(str.substring(0, index));
        // only if abbreviation has occurred do we append the appendToEnd value
        if (index < str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }

    return result.toString();
}