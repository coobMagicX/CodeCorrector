public static long skip(InputStream input, long numToSkip) throws IOException {
    long available = numToSkip;
    while (numToSkip > 0) {
        long skipped = input.skip(numToSkip);
        if (skipped == 0) { // When skip does not work as expected
            if (input.read() == -1) { // Try reading byte-by-byte. If -1, end of stream reached.
                break;
            }
            numToSkip -= 1; // Successfully read one byte, reduce numToSkip
        } else {
            numToSkip -= skipped; // Skip was successful, reduce numToSkip by the number of bytes skipped
        }
    }
    return available - numToSkip;
}