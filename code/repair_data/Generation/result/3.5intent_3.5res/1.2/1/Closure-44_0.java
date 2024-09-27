void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    char lastChar = getLastChar();
    
    if ((isWordChar(c) || c == '\') && isWordChar(lastChar)) {
        // need space to separate. This is not pretty printing.
        // For example: "return foo;"
        append(" ");
    } else if (c == '/' && lastChar == '/') {
        // Do not allow a forward slash to appear right after another forward slash.
        append(" ");
    }

    append(newcode);
}
