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

    append(newcode);
}