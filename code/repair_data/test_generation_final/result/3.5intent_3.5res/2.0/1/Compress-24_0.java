public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    long result = 0;
    int end = offset + length;
    int start = offset;

    if (length < 2) {
        throw new IllegalArgumentException("Length " + length + " must be at least 2");
    }

    if (buffer[start] == 0) {
        return 0L;
    }

    // Skip leading spaces
    while (start < end && buffer[start] == ' ') {
        start++;
    }

    // Trim all trailing NULs and spaces.
    // The ustar and POSIX tar specs require a trailing NUL or
    // space but some implementations use the extra digit for big
    // sizes/uids/gids ...
    while (end > start && (buffer[end - 1] == 0 || buffer[end - 1] == ' ')) {
        end--;
    }

    for (; start < end; start++) {
        final byte currentByte = buffer[start];
        if (currentByte < '0' || currentByte > '7') {
            throw new IllegalArgumentException(
                    exceptionMessage(buffer, offset, length, start, currentByte));
        }
        try {
            result = Math.addExact(Math.multiplyExact(result, 8), (currentByte - '0'));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    exceptionMessage(buffer, offset, length, start, currentByte));
        }
    }

    return result;
}