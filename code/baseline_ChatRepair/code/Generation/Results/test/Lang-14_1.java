public static boolean equals(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    if (cs1.length() != cs2.length()) {
        return false;
    }
    for (int i = 0; i < cs1.length(); i++) {
        if (cs1.charAt(i)