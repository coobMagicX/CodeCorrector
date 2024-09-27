public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
    if (cs1 == cs2) {
        return true;
    }
    if (cs1 == null || cs2 == null) {
        return false;
    }
    String str1 = newStringUtf8(cs1.toString().getBytes());
    String str2 = newStringUtf8(cs2.toString().getBytes());
    return str1.equals(str2);
}