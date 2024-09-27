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
    while (start < end) {
        if (buffer[start] == ' ') {
            start++;
        } else {
            break;
        }
    }

    // Trim all trailing NULs and spaces.
    // The ustar and POSIX tar specs require a trailing NUL or
    // space but some implementations use the extra digit for big
    // sizes/uids/gids ...
    byte trailer = buffer[end - 1];
    while (start < end && (trailer == 0 || trailer == ' ')) {
        end--;
        trailer = buffer[end - 1];
    }
    if (start == end) {
        throw new IllegalArgumentException(
                exceptionMessage(buffer, offset, length, start, trailer));
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
    // default charset is good enough for an exception message,
    //
    // the alternative was to modify parseOctal and
    // parseOctalOrBinary to receive the ZipEncoding of the
    // archive (deprecating the existing public methods, of
    // course) and dealing with the fact that ZipEncoding#decode
    // can throw an IOException which parseOctal* doesn't declare
    String string = new String(buffer, offset, length);

    string = string.trim(); // Trim leading and trailing spaces
    string = string.replaceAll("\0", "{NUL}"); // Replace NULs to allow string to be printed
    final String s = "Invalid byte " + currentByte + " at offset " + (current - offset) + " in '" + string + "' len=" + length;
    return s;
}