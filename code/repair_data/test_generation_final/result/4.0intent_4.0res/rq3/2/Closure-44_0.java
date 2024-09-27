class CodeBuilder {
    private StringBuilder sb = new StringBuilder();
    private char lastChar = '\0';

    void maybeEndStatement() {
        if (sb.length() > 0 && lastChar != ';') {
            sb.append(';');
        }
    }

    char getLastChar() {
        return lastChar;
    }

    void append(String str) {
        sb.append(str);
        if (str.length() > 0) {
            lastChar = str.charAt(str.length() - 1);
        }
    }

    boolean isWordChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    void add(String newcode) {
        maybeEndStatement();

        if (newcode.length() == 0) {
            return;
        }

        char c = newcode.charAt(0);
        if ((isWordChar(c) || c == '/') &&
            isWordChar(getLastChar())) {
            // need space to separate. This is not pretty printing.
            // For example: "return foo;"
            append(" ");
            // Do not allow a forward slash to appear after a DIV.
            // For example,
            // REGEXP DIV REGEXP
            // is valid and should print like
            // / // / /
        }

        append(newcode);
    }
}