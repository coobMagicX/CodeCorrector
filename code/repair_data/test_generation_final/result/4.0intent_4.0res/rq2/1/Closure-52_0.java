static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) return false;  // Ensure non-empty string to be a number
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return true;
}