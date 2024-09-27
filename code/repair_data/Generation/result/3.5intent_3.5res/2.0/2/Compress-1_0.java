public void close() throws IOException {
    if (!this.closed) {
        super.close();
        this.closed = true;
    } else {
        throw new IOException("Stream already closed");
    }
}

public void testCpioUnarchive() {
    // test case code here
}