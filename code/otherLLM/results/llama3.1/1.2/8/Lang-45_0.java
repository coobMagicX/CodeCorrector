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
    int effectiveUpper = Math.min(Math.max(upper, str.length()), lower);

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", effectiveUpper);
    if (index == -1) {
        result.append(str.substring(0, effectiveUpper));
        // only if abbreviation has occured do we append the appendToEnd value
        if (effectiveUpper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > effectiveUpper) {
        result.append(str.substring(0, effectiveUpper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str.substring(0, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }
    return result.toString();
}