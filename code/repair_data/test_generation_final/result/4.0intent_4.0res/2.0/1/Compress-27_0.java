public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    long result = 0;
    int end = offset + length;
    int start = offset;

    if (length < 2) {
        throw new IllegalArgumentException("Length " + length + " must be at least 2");
    }

    // Skip leading spaces and NUL bytes
    while (start < end && (buffer[start] == ' ' || buffer[start] == 0)) {
        start++;
    }

    // Trim all trailing NULs and spaces.
    // The ustar and POSIX tar specs require a trailing NUL or
    // space but some implementations use the extra digit for big
    // sizes/uids/gids ...
    while (end > start && (buffer[end - 1] == 0 || buffer[end - 1] == ' ')) {
        end--;
    }

    if (start == end) {
        throw new IllegalArgumentException(
                exceptionMessage(buffer, offset, length, start, buffer[end]));
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

private static String exceptionMessage(byte[] buffer, final int offset,
                                       final int length, int current, final byte currentByte) {
    String string = new String(buffer, offset, length);

    string = string.replaceAll("\0", "{NUL}"); // Replace NULs to allow string to be printed
    final String s = "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
    return s;
}