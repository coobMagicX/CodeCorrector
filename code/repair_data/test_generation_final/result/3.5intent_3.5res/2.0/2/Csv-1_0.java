public int read() throws IOException {
    int current = super.read();
    if (current == '\n') {
        lineCounter++;
    } else if (current == '\r') {
        int nextChar = super.read();
        if (nextChar != '\n') {
            super.unread(nextChar);
        }
        lineCounter++;
    }
    lastChar = current;
    return lastChar;
}