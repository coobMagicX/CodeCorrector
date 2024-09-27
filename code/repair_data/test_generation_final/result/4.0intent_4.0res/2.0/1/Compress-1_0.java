public void close() throws IOException {
    if (!this.closed) {
        finish();  // Ensure all contents are written and entries are closed properly
        super.close();
        this.closed = true;
    }
}