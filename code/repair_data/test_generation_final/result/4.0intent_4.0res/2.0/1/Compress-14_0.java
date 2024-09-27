public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    long result = 0;
    int end = offset + length;
    int start = offset;

    if (length < 2) {
        throw new IllegalArgumentException("Length " + length + " must be at least 2");
    }

    boolean allNUL = true;
    for (int i = start; i < end; i++) {
        if (buffer[i] != 0) {
            allNUL = false;
            break;
        }
    }
    if (allNUL) {
        return 0L;
    }

    // Skip leading spaces
    while (start < end) {
        if (buffer[start] == ' ') {
            start++;
        } else {
            break;
        }
    }

    // Must have trailing NUL or space
    byte trailer;
    trailer = buffer[end - 1];
    if (trailer == 0 || trailer == ' ') {
        end--;
    } else {
        throw new IllegalArgumentException(
                exceptionMessage(buffer, offset, length, end - 1, trailer));
    }
    // May have additional NUL or space
    trailer = buffer[end - 1];
    if (trailer == 0 || trailer == ' ') {
        end--;
    }

    for (; start < end; start++) {
        final byte currentByte = buffer[start];
        // CheckStyle:MagicNumber OFF
        if (currentByte < '0' || currentByte > '7') {
            if (currentByte != 0 && currentByte != ' ') {
                throw new IllegalArgumentException(
                        exceptionMessage(buffer, offset, length, start, currentByte));
            }
            continue; // skip non-octal characters, might be corruption or non-standard format
        }
        result = (result << 3) + (currentByte - '0'); // convert from ASCII
        // CheckStyle:MagicNumber ON
    }

    return result;
}

private static String exceptionMessage(byte[] buffer, int offset, int length, int position, byte badByte) {
    return String.format("Invalid header byte at position %d, byte = %d, within buffer range [%d, %d]",
            position, badByte, offset, offset + length - 1);
}

public static long parseOctalOrBinary(final byte[] buffer, final int offset, final int length) {
    if ((buffer[offset] & 0x80) == 0) {
        return parseOctal(buffer, offset, length);
    }

    long val = buffer[offset] & 0x7f;
    for (int i = 1; i < length; i++) {
        if (val >= (1L << (63 - 8))) {
            throw new IllegalArgumentException(
                "At offset " + offset + ", " + length + " byte " +
                "binary number exceeds maximum signed long value");
        }
        val = (val << 8) + (buffer[offset + i] & 0xff);
    }
    return val;
}