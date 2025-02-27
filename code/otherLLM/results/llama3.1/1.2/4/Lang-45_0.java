public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    // validate input parameters
    if (lower < 0 || upper > str.length()) {
        throw new IndexOutOfBoundsException("Invalid indices");
    }

    // if the lower value is greater than the length of the string,
    // set to 0
    // if the upper value is -1 (i.e. no limit) or is greater
    // than the length of the string, set to the length of the string
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    // if upper is less than lower, raise it to lower
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1 || index > upper) {
        result.append(str.substring(0, upper));
        // only if abbreviation has occured do we append the appendToEnd value
        if (upper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else {
        result.append(str.substring(0, index));
        if (index < upper) {
            result.append(str.substring(index + 1, upper));
            // only if abbreviation has occured do we append the appendToEnd value
            if (upper != str.length()) {
                result.append(StringUtils.defaultString(appendToEnd));
            }
        } else {
            result.append(str.substring(index + 1));
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }
    return result.toString();
}