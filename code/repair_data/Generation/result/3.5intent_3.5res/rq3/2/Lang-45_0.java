public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return "";
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
    int index = str.indexOf(' ', lower);
    if (index == -1) {
        result.append(str.substring(0, upper));
        // only if abbreviation has occurred do we append the appendToEnd value
        if (upper != str.length()) {
            result.append(appendToEnd);
        }
    } else if (index > upper) {
        result.append(str.substring(0, upper));
        result.append(appendToEnd);
    } else {
        result.append(str.substring(0, index));
        result.append(appendToEnd);
    }
    return result.toString();
}