public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    long result = 0;
    int end = offset + length;
    int start = offset;

    if (length < 2) {
        throw new IllegalArgumentException("Length " + length + " must be at least 2");
    }

    // Skip leading spaces and NULs
    while (start < end && (buffer[start] == ' ' || buffer[start] == 0)) {
        start++;
    }

    // Find the position just before the first trailing NUL or space
    int newEnd = end;
    while (newEnd > start && (buffer[newEnd - 1] == ' ' || buffer[newEnd - 1] == 0)) {
        newEnd--;
    }

    // Set end to newEnd, where newEnd is the position of last relevant byte + 1
    end = newEnd;

    for (int i = start; i < end; i++) {
        final byte currentByte = buffer[i];

        // Check if the byte is valid octal digit
        if (currentByte < '0' || currentByte > '7') {
            throw new IllegalArgumentException(
                    exceptionMessage(buffer, offset, length, i, currentByte));
        }

        result = (result << 3) + (currentByte - '0'); // convert from ASCII to numeric value
    }

    return result;
}

private static String exceptionMessage(byte[] buffer, final int offset,
                                       final int length, int current, final byte currentByte) {
    String string = new String(buffer, offset, length); // TODO default charset?
    string = string.replaceAll("\0", "{NUL}"); // Replace NULs to allow string to be printed
    final String s = "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
    return s;
}