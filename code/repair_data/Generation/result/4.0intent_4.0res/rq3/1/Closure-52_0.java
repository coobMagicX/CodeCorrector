static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) return false; // Check if the string is empty
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!Character.isDigit(c)) { // Check if the character is not a digit
            return false;
        }
    }
    return true;
}