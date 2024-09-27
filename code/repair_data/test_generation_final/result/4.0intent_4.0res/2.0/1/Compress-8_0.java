public static long parseOctal(final byte[] buffer, final int offset, final int length) {
    if (buffer == null || length == 0) {
        throw new IllegalArgumentException("Buffer is empty or null");
    }

    long result = 0;
    boolean stillPadding = true;
    boolean nonPaddingStarted = false;
    int end = offset + length;

    for (int i = offset; i < end; i++) {
        final byte currentByte = buffer[i];
        if (currentByte == 0) {
            if (i == offset) { // check if NUL is at the start
                throw new IllegalArgumentException("NUL character at the start of the buffer");
            }
            break; // valid end of octal string
        }

        // Skip leading spaces or zeros
        if ((currentByte == (byte) ' ' || currentByte == '0') && stillPadding) {
            continue;
        }

        if (currentByte == (byte) ' ') {
            if (nonPaddingStarted) { // space after numbers are started
                throw new IllegalArgumentException("Space character within octal number");
            }
            break; // end of padding and start of octal number
        }

        if (currentByte < '0' || currentByte > '7') {
            throw new IllegalArgumentException(exceptionMessage(buffer, offset, length, i, currentByte));
        }

        stillPadding = false;
        nonPaddingStarted = true;
        result = (result << 3) + (currentByte - '0'); // convert from ASCII
    }

    if (nonPaddingStarted && (buffer[end - 1] != 0 && buffer[end - 1] != (byte) ' ')) {
        throw new IllegalArgumentException("Octal string must terminate with NUL or space");
    }

    return result;
}

private static String exceptionMessage(byte[] buffer, final int offset, final int length, int current, final byte currentByte) {
    String string = new String(buffer, offset, length);
    string = string.replaceAll("\0", "{NUL}"); // Replace NULs to allow string to be printed
    final String s = "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
    return s;
}