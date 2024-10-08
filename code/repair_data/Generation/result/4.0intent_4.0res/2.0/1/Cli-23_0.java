protected StringBuffer renderWrappedText(StringBuffer sb, int width, 
                                         int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }
    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    // all following lines must be padded with nextLineTabStop space 
    // characters
    final String padding = createPadding(nextLineTabStop);

    while (true) {
        text = text.substring(pos).trim();
        if (text.isEmpty()) {
            return sb;
        }
        text = padding + text;
        pos = findWrapPos(text, width, 0);

        if (pos == -1 || text.length() <= width) {
            sb.append(text);
            return sb;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}