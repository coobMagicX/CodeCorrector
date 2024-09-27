public static String parseName(byte[] buffer, final int offset, final int length) {
    StringBuilder result = new StringBuilder(length);
    int end = offset + length;

    for (int i = offset; i < end; ++i) {
        if (buffer[i] == 0) {
            break;
        }
        if (buffer[i] < 0) {
            result.append((char) (buffer[i] & 0xFF));
        } else {
            result.append((char) buffer[i]);
        }
    }

    return result.toString();
}