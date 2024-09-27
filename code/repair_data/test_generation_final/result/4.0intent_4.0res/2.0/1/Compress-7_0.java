public static String parseName(byte[] buffer, final int offset, final int length) {
    StringBuffer result = new StringBuffer(length);
    int end = offset + length;

    // Ensure proper encoding by specifying the charset
    String str = new String(buffer, offset, length, java.nio.charset.StandardCharsets.UTF_8);

    for (int i = 0; i < str.length(); ++i) {
        if (str.charAt(i) == '\0') {
            break;
        }
        result.append(str.charAt(i));
    }

    return result.toString();
}