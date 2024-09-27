public static int formatLongOctalOrBinaryBytes(final long value, final byte[] buf, final int offset, final int length) {

    // Check whether we are dealing with UID/GID or SIZE field
    final long maxAsOctalChar = length == TarConstants.UIDLEN ? TarConstants.MAXID : TarConstants.MAXSIZE;

    final boolean negative = value < 0;
    if (!negative && value <= maxAsOctalChar) { // OK to store as octal chars
        return formatLongOctalBytes(value, buf, offset, length);
    }

    if (length == 8) {
        if (isBinary(value)) {
            formatLongBinary(value, buf, offset, length, negative);
        } else {
            formatLongOctalBytes(value, buf, offset, length);
        }
    } else {
        formatBigIntegerBinary(value, buf, offset, length, negative);
    }

    buf[offset] = (byte) (negative ? 0xff : 0x80);
    return offset + length;
}

private static boolean isBinary(long value) {
    return Long.bitCount(value) > 1;
}