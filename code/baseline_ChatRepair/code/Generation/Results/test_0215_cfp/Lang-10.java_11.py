private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);

        // Handle whitespace: compress multiple whitespace characters into a regex pattern
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {  // First whitespace character after non-whitespace characters
                regex.append("\\s+");  // Regex pattern for one or more whitespace characters
                wasWhite = true;
            }
            continue;  // Skip adding the character, pattern already added
        }
        wasWhite = false;

        // Handle unquoting (skip the character if it's a quote and unquote is true)
        if (c == '\'' && unquote) {
            continue;  // Skip appending the quote ' if unquoting is specified
        }

        // Escaping of regex special characters
        if ("?[](){}\\|*+^$.#".indexOf(c) != -1) {  // Checks if c is a special regex character
            regex.append('\\');  // Escape the character
        }

        regex.append(c);  // Append the current character or the escaped one to the regex
    }

    return regex;
}
