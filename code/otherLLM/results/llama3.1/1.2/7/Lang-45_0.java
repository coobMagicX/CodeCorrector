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
    if (lower < 0) {
        lower = 0;
    }
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1) {
        result.append(str.substring(0, Math.min(lower, upper)));
        // only if abbreviation has occured do we append the appendToEnd value
        if (Math.min(lower, upper) != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > Math.min(lower, upper)) {
        result.append(str.substring(0, Math.min(lower, upper)));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        int start = lower;
        int end = Math.min(lower + 1, index);
        while ((end - start) <= upper && start < str.length()) {
            if (str.charAt(start) == ' ') {
                start++;
                continue;
            }
            end = str.indexOf(' ', start + 1);
            if (end >= 0) {
                result.append(str.substring(start, end));
                start = end + 1;
            } else {
                result.append(str.substring(start));
                break;
            }
        }
        // only if abbreviation has occured do we append the appendToEnd value
        if (Math.min(lower, upper) != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    }
    return result.toString();
}