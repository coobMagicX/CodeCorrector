public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    long result = 0;
    int end = offset + length;
    int start = offset;

    // Check if the buffer is empty or nearly empty, avoiding unnecessary exceptions
    if (length == 0 || (length == 1 && (buffer[start] == ' ' || buffer[start] == 0))) {
        return 0L;
    }

    // Skip leading spaces and nulls
    while (start < end && (buffer[start] == ' ' || buffer[start] == 0)) {
        start++;
    }

    // Trim all trailing NULs and spaces.
    while (end > start && (buffer[end - 1] == 0 || buffer[end - 1] == ' ')) {
        end--;
    }

    // Now parse the actual octal number
    for (int i = start; i < end; i++) {
        final byte currentByte = buffer[i];
        if (currentByte < '0' || currentByte > '7') {
            throw new IllegalArgumentException(
                    exceptionMessage(buffer, offset, length, i, currentByte));
        }
        result = (result << 3) + (currentByte - '0'); // convert from ASCII
    }

    return result;
}

private static String exceptionMessage(byte[] buffer, final int offset,
                                       final int length, int current, final byte currentByte) {
    String string = new String(buffer, offset, length);
    string = string.replaceAll("\0", "{NUL}"); // Replace NULs to allow string to be printed
    return "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
}