public static int formatLongOctalBytes(
    final long value, final byte[] buf, final int offset, final int length) {
    // implementation of formatting long value as octal bytes
}

public static void formatLongBinary(
    final long value, final byte[] buf, final int offset, final int length, final boolean negative) {
    // implementation of formatting long value as binary
}

public static void formatBigIntegerBinary(
    final long value, final byte[] buf, final int offset, final int length, final boolean negative) {
    // implementation of formatting big integer value as binary
}

public static int testRoundTripOctalOrBinary(final long value) {
    byte[] buf = new byte[16];
    int offset = 0;
    int length = 11;
    formatLongOctalOrBinaryBytes(value, buf, offset, length);
    long result = parseOctalOrBinaryBytes(buf, offset, length);
    return result == value ? 0 : -1;
}