static boolean isSimpleNumber(String s) {
    int len = s.length();
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (!Character.isDigit(c)) {
            return false;
        }
    }
    return len > 0 && Character.isDigit(s.charAt(0));
}