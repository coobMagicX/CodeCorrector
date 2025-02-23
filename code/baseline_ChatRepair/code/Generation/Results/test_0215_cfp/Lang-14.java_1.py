public static boolean equals(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    int length = cs1.length();
    if (length != cs2.length()) {
        return false;
    }
    for (int i = 0; i < length; i++) {
        if (cs1.charAt(i) != cs2.charAt(i)) {
            return false;
        }
    }
    return true;
}
