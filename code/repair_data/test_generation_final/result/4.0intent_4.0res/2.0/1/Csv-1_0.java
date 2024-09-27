public int read() throws IOException {
    int current = super.read();
    if (current == '\n') {
        if (lastChar != '\r') {
            lineCounter++;
        }
    } else if (current == '\r') {
        lineCounter++;
    }
    lastChar = current;
    return lastChar;
}