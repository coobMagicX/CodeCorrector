private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+");
            }
            continue;
        }
        wasWhite = false;

        if (c == '\'' && unquote) {
            if (i + 1 < value.length()) {
                i++;  // safely increment i and update character c to the next character in the value
                c = value.charAt(i);
            } else {
                continue;  // skip processing and adding an additional character since its a final quote
            }
        }

        // Check if the character is a special regex character that needs to be escaped
        if ("?[](){}\\|*+^$.".indexOf(c) != -1) {
            regex.append('\\');
        }

        // Append the (possibly updated) character to the regex
        regex.append(c);
    }
    return regex;
}
