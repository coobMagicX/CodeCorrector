private String consumeTo(char c) {
    int start = pos;
    while (pos < buf.length) {
        char current = buf[pos];
        if (current == c) {
            String consumed = new String(buf, start, pos - start);
            pos++; // move past the character
            return consumed;
        }
        pos++;
    }
    // Reset pos to the end of the buffer if character c is not found
    pos = buf.length;
    return ""; // return empty string if end character is not found
}