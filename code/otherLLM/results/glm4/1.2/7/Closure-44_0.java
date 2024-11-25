void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char c = newcode.charAt(0);
    boolean lastCharWasWord = isWordChar(getLastChar());
    
    // Handle spaces around slashes and ensure not to append a space before a slash.
    for (int i = 0; i < newcode.length(); i++) {
        char currentChar = newcode.charAt(i);
        
        if ((isWordChar(currentChar) || currentChar == '\\') &&
            lastCharWasWord) {
            append(" ");
        }
        
        // Check for consecutive slashes and handle accordingly.
        if (i > 0 && currentChar == '/' && newcode.charAt(i - 1) == '/') {
            append("//"); // Append the slashes separated by a space
        } else {
            append(String.valueOf(currentChar));
        }
        
        lastCharWasWord = isWordChar(currentChar);
    }
}