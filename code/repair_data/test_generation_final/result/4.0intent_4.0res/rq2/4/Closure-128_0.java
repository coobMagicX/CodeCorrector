static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0 || s.charAt(0) == '0') { // Check if string is empty or starts with '0' first
        return false;
    }
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false; // Return false if any character is not a digit
        }
    }
    return true; // Return true if all characters are digits and it does not start with '0'
}