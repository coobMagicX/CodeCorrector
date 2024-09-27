void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    boolean isNewCodeRegex = newcode.startsWith("/") && newcode.endsWith("/");
    // Check if the first character of newcode is a word character or a part of regex, and the last character is also a word character
    if (isWordChar(getLastChar()) && (isWordChar(c) || isNewCodeRegex)) {
        append(" ");  // need space to separate words or regex from previous token
    }

    append(newcode);
}