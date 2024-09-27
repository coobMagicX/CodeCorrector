void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    char lastChar = getLastChar();
    if ((isWordChar(c) || c == '\\') && isWordChar(lastChar)) {
        // need space to separate. This is not pretty printing.
        // For example: "return foo;"
        append(" ");
    } else if (c == '/' && lastChar == '/') {
        // Handling consecutive slashes for regex to prevent them being treated as comments
        append(" ");
    }

    append(newcode);
}

static boolean isWordChar(char ch) {
    return (ch == '_' ||
            ch == '$' ||
            Character.isLetterOrDigit(ch));
}