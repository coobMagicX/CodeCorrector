public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
    if (cs1 == cs2) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    if (cs1 instanceof String && cs2 instanceof String) {
        return cs1.equals(cs2);
    }
    if (cs1.length() != cs2.length()) {
        return false;
    }
    for (int i = 0; i < cs1.length(); i++) {
        if (cs1.charAt(i) != cs2.charAt(i)) {
            return false;
        }
    }
    return true;
}