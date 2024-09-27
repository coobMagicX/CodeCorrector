protected StringBuffer renderWrappedText(StringBuffer sb, int width, 
                                         int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }

    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    final String padding = createPadding(nextLineTabStop);

    while (true) {
        text = padding + text.substring(pos).trim();
        pos = findWrapPos(text, width, nextLineTabStop);

        if (pos == -1) {
            sb.append(text);
            return sb;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}

private int findWrapPos(String text, int width, int nextLineTabStop) {
    int pos = width;

    if (pos >= text.length()) {
        return -1;
    }

    if (text.charAt(pos) == ' ') {
        return pos;
    }

    while (pos > nextLineTabStop && text.charAt(pos) != ' ') {
        pos--;
    }

    return pos;
}