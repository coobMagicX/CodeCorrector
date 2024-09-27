void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    char lastChar = getLastChar();

    // Checking if both the last character and the new character are slashes,
    // which might incorrectly form a comment if not handled properly.
    if (lastChar == '/' && c == '/') {
        append(" "); // Insert a space to prevent forming a comment
    } else if ((isWordChar(c) || c == '\\') && isWordChar(lastChar)) {
        // Need space to separate word characters or escape sequences without merging
        append(" ");
    }

    append(newcode);
}

static boolean isWordChar(char ch) {
    return (ch == '_' || ch == '$' || Character.isLetterOrDigit(ch));
}