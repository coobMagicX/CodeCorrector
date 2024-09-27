protected int findWrapPos(String text, int width, int startPos) {
    int pos;

    // the line ends before the max wrap pos or a new line char found
    if (((pos = text.indexOf('\n', startPos)) != -1 && pos <= startPos + width)
            || ((pos = text.indexOf('\t', startPos)) != -1 && pos <= startPos + width)) {
        return pos;
    } else if (startPos + width >= text.length()) {
        return text.length();
    }

    // look for the last whitespace character before startPos+width
    pos = startPos + width;

    if (pos < text.length() && (text.charAt(pos) == ' ' || text.charAt(pos) == '\n' || text.charAt(pos) == '\r')) {
        return pos;
    }

    while (pos > startPos && (text.charAt(pos) != ' ' && text.charAt(pos) != '\n' && text.charAt(pos) != '\r')) {
        --pos;
    }

    // if we found it - just return
    if (pos > startPos) {
        return pos;
    } else {
        // if we didn't find one, simply chop at startPos+width
        pos = startPos + width;
        if (pos < text.length()) {
            while (pos < text.length() && (text.charAt(pos) != ' ' && text.charAt(pos) != '\n' && text.charAt(pos) != '\r')) {
                ++pos;
            }
            return pos == text.length() ? -1 : pos;
        } else {
            return text.length();
        }
    }
}