static boolean isSimpleNumber(String s) {
    if (s.length() == 0) {
        return false;
    }
    // Check for hexadecimal
    if (s.startsWith("0x") || s.startsWith("0X")) {
        try {
            Integer.parseInt(s.substring(2), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Check for octal
    if (s.startsWith("0") && s.length() > 1 && !s.contains("8") && !s.contains("9")) {
        try {
            Integer.parseInt(s, 8);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Check for decimal
    try {
        Integer.parseInt(s);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}