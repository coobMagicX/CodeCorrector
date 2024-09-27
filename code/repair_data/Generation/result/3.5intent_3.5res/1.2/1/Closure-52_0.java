static boolean isSimpleNumber(String s) {
    int len = s.length();
    boolean hasNonZero = false;
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            if (c == '0' && !hasNonZero && len > 1) {
                continue; // Skip leading zeros
            } else {
                return false;
            }
        }
        if (c != '0') {
            hasNonZero = true;
        }
    }
    return len > 0;
}