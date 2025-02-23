private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    int i = 0;

    while (i < value.length()) {
        char c = value.charAt(i);

        // Handling whitespace compression into a single regex pattern that matches one or more whitespaces.
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                regex.append("\\s+");
                wasWhite = true;
            }
            // Move to next character
            i++;
            continue; // Skip the rest of the loop for whitespace.
        }

        // Reset whitespace flag for non-whitespace characters.
        wasWhite = false;

        // Handle character escaping, stepping over the literal if `unquote` is true.
        if (unquote && c == '\'') {
            if (i + 1 < value.length()) {
                i++; // Skip the quote itself
                c = value.charAt(i); // Move to the following character
            } else {
                // If there's no character after quote, just break out of the loop.
                break;
            }
        }

        // Add escape character in regex for special regex symbols.
        switch (c) {
            case '?':
            case '[':
            case ']':
            case '(':
            case ')':
            case '{':
            case '}':
            case '\\':
            case '|':
            case '*':
            case '+':
            case '^':
            case '$':
            case '.':
                regex.append('\\');
                // No `break` here because we need to append 'c' after escaping.
        }

        // Append the current character which might be changed if 'unquote' condition was true.
        regex.append(c);
        i++;
    }

    return regex;
}
