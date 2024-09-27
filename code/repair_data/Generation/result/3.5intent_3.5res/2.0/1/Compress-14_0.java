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
            throw new IllegalArgumentException(
                    exceptionMessage(buffer, offset, length, start, currentByte));
        }
        result = (result << 3) + (currentByte - '0'); // convert from ASCII
        // CheckStyle:MagicNumber ON
    }

    return result;
}

public static void formatUnsignedOctalString(final long value, byte[] buffer,
                                             final int offset, final int length) {
    int remaining = length;
    remaining--;
    if (value == 0) {
        buffer[offset + remaining--] = (byte) '0';
    } else {
        long val = value;
        for (; remaining >= 0 && val != 0; --remaining) {
            // CheckStyle:MagicNumber OFF
            buffer[offset + remaining] = (byte) ((byte) '0' + (byte) (val & 7));
            val = val >>> 3;
            // CheckStyle:MagicNumber ON
        }
        if (val != 0) {
            throw new IllegalArgumentException(
                    value + "=" + Long.toOctalString(value) + " will not fit in octal number buffer of length " + length);
        }
    }

    for (; remaining >= 0; --remaining) { // leading zeros
        buffer[offset + remaining] = (byte) '0';
    }
}

public static String exceptionMessage(final byte[] buffer, final int offset, final int length,
                                      final int problemOffset, final byte problemValue) {
    StringBuilder buf = new StringBuilder(74);
    buf.append("Invalid byte ").append(problemValue).append(" at offset ").append(problemOffset).append(" in '");
    for (int i = offset; i < offset + length; i++) {
        buf.append((char) buffer[i]);
    }
    buf.append('\'');
    return buf.toString();
}