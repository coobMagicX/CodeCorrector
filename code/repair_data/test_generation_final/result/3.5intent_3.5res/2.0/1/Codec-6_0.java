public int read(byte b[], int offset, int len) throws IOException {
    if (b == null) {
        throw new NullPointerException();
    } else if (offset < 0 || len < 0) {
        throw new IndexOutOfBoundsException();
    } else if (offset > b.length || offset + len > b.length) {
        throw new IndexOutOfBoundsException();
    } else if (len == 0) {
        return 0;
    } else {
        if (!base64.hasData()) {
            byte[] buf = new byte[doEncode ? 4096 : 8192];
            int c = in.read(buf);
            // A little optimization to avoid System.arraycopy()
            // when possible.
            if (c > 0 && b.length == len) {
                base64.setInitialBuffer(b, offset, len);
            }
            if (doEncode) {
                base64.encode(buf, 0, c);
            } else {
                base64.decode(buf, 0, c);
            }
        }
        int readLen = base64.readResults(b, offset, len);
        // Check if the end-of-stream indicator is returned and if there is more data to be read
        if (readLen == -1 && base64.hasData()) {
            // Call the read method again to read the remaining data
            int remainingLen = len - readLen;
            int additionalReadLen = base64.readResults(b, offset + readLen, remainingLen);
            if (additionalReadLen != -1) {
                readLen += additionalReadLen;
            } else if (base64.hasData()) {
                // If there is still more data to be read after the end-of-stream indicator,
                // return 0 to indicate that there is no more data available at the moment
                readLen = 0;
            }
        }
        return readLen;
    }
}