protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }

    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    // all following lines must be padded with nextLineTabStop space characters
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

public void setWidth(int width) {
    this.defaultWidth = width;
}

private int findWrapPos(String text, int width, int tabStop) {
    int pos = width;
    if (text.length() <= width) {
        return -1;
    }
    while (pos > 0 && !Character.isWhitespace(text.charAt(pos - 1))) {
        pos--;
        if (pos == tabStop) {
            pos = width;
        }
    }
    return pos;
}

private String createPadding(int tabStop) {
    StringBuilder padding = new StringBuilder();
    for (int i = 0; i < tabStop; i++) {
        padding.append(" ");
    }
    return padding.toString();
}

private String rtrim(String text) {
    int i = text.length() - 1;
    while (i >= 0 && Character.isWhitespace(text.charAt(i))) {
        i--;
    }
    return text.substring(0, i + 1);
}