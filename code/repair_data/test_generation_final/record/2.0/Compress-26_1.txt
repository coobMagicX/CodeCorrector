public static long skip(InputStream input, long numToSkip) throws IOException {
    long available = numToSkip;
    while (numToSkip > 0) {
        long skipped = input.skip(numToSkip);
        if (skipped == 0) {
            int byteRead = input.read();
            if (byteRead == -1) {
                break;
            }
            skipped = 1;
        }
        numToSkip -= skipped;
    }
    
    return available - numToSkip;
}