private final static int _parseIndex(String str) {
    final int len = str.length();
    if (len == 0 || len > 10 || (len > 1 && str.charAt(0) == '0')) {
        return -1;
    }
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (c > '9' || c < '0') {
            return -1;
        }
    }
    if (len == 10) {
        long l = NumberInput.parseLong(str);
        if (l > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) l;
    }
    return Integer.parseInt(str);
}