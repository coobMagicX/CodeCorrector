public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    // if the lower value is greater than the length of the string,
    // set to the length of the string
    // if the upper value is -1 (i.e. no limit) or is greater
    // than the length of the string, set to the length of the string
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    // if upper is less than lower, raise it to lower
    if (upper < lower) {
        upper = lower;
    }

    StringBuilder result = new StringBuilder();
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1) {
        result.append(str, 0, upper);
        // only if abbreviation has occurred do we append the appendToEnd value
        if (upper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > upper) {
        result.append(str, 0, upper);
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str, 0, index);
        if (upper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }
    return result.toString();
}