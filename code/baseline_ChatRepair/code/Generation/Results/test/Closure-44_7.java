void add(String newcode) {
    maybeEndStatement();

    if (newcode.length() == 0) {
        return;
    }

    char firstChar = newcode.charAt(0);
    char lastChar = getLastChar();

    
    if (isWordChar(firstChar) && isWordChar(lastChar)) {
        append(" ");
    } else if (lastChar == '/' && firstChar == '/') {
        
        append(" ");
    }

    append(newcode);
}
