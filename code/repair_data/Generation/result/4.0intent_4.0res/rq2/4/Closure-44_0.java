void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    if ((isWordChar(c) || c == '\\') && isWordChar(getLastChar())) {
        // need space to separate. This is not pretty printing.
        // For example: "return foo;"
        append(" ");
    }

    // Handling regular expressions and forward slashes correctly
    if (c == '/' && getLastChar() == '/') {
        // Do not insert space if it's likely a regular expression
        append(newcode);
    } else if (c == '/' && getLastChar() == ' ') {
        // Check if this is a division or start of regex, additional logic might be needed here
        append(newcode);
    } else {
        // General case for all other characters
        append(newcode);
    }
}