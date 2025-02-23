public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }

    char[] chars = str.toCharArray();
    int length = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean hasSign = false;
    int startIndex = (chars[0] == '-' || chars[0] == '+') ? 1 : 0; // allowing leading + or -

    for (int i = startIndex; i < length; i++) {
        char c = chars[i];

        // Check if it's a valid digit
        if (Character.isDigit(c)) {
            hasSign = false; // reset sign flag if we find a digit
        } else if (c == '.') {
            // Check if we've already encountered a decimal point or exponent
            if (hasDecPoint || hasExp) {
                return false;
            }
            hasDecPoint = true;
        } else if (c == 'e' || c == 'E') {
            // Check if we've already encountered an exponent, if there are no digits before the exponent, or if it's the last character
            if (hasExp || i == startIndex || i == length - 1) {
                return false;
            }
            hasExp = true;
            hasSign = true; // expect a sign or digit next
        } else if (c == '+' || c == '-') {
            // The sign must come immediately after an exponent and must not be the last character
            if (!hasExp || hasSign || i == length - 1) {
                return false;
            }
            hasSign = true; // next character must be a digit
        } else {
            // If we encounter any other character, it's not a valid number
            return false;
        }
    }
    return true;
}
