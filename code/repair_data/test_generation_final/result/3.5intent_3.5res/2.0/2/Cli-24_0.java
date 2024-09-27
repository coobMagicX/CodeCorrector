protected StringBuffer renderWrappedText(StringBuffer sb, int width, 
                                     int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }

    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    if (nextLineTabStop >= width) {
        // stops infinite loop happening
        throw new IllegalStateException("Total width is less than the width of the argument and indent " + 
                                        "- no room for the description");
    }

    // all following lines must be padded with nextLineTabStop space 
    // characters
    final String padding = createPadding(nextLineTabStop);

    while (pos != -1) {
        text = padding + text.substring(pos).trim();
        pos = findWrapPos(text, width, nextLineTabStop);

        if (pos == -1) {
            sb.append(text);
            return sb;
        }

        if (text.length() > width && pos == nextLineTabStop - 1) {
            pos = width;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }

    return sb;
}

private int findWrapPos(String text, int width, int startPos) {
    int pos = text.indexOf('\n', startPos);
    if (pos != -1 && pos <= width) {
        return pos + 1;
    }

    pos = text.indexOf('\t', startPos);
    if (pos != -1 && pos <= width) {
        return pos + 1;
    }

    if (startPos + width >= text.length()) {
        return -1;
    }

    pos = startPos + width;

    // Search for the last whitespace character before startPos + width
    while (pos >= startPos) {
        if (Character.isWhitespace(text.charAt(pos))) {
            return pos + 1;
        }
        pos--;
    }

    // If no whitespace character is found, return startPos + width
    return startPos + width;
}

private String rtrim(String text) {
    int pos = text.length() - 1;
    while (pos >= 0 && Character.isWhitespace(text.charAt(pos))) {
        pos--;
    }
    return text.substring(0, pos + 1);
}

private String createPadding(int tabStop) {
    StringBuilder padding = new StringBuilder();
    for (int i = 0; i < tabStop; i++) {
        padding.append(' ');
    }
    return padding.toString();
}