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
        nextLineTabStop = width - 1;
    }

    // all following lines must be padded with nextLineTabStop space 
    // characters
    final String padding = createPadding(nextLineTabStop);

    while (true) {
        text = padding + text.substring(pos).trim();
        pos = findWrapPos(text, width, nextLineTabStop); // Fix: Pass nextLineTabStop as the starting position for findWrapPos

        if (pos == -1) {
            sb.append(text);

            return sb;
        }
        
        if ((text.length() > width) && (pos == nextLineTabStop - 1)) {
            pos = width;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}

protected int findWrapPos(String text, int width, int startPos) {
    int pos = text.indexOf(' ', startPos);

    if (pos != -1 && pos <= width) {
        return pos;
    }
    
    return width;
}

protected String rtrim(String text) {
    int len = text.length();
    int st = 0;

    while ((st < len) && (text.charAt(len - 1) <= ' ')) {
        len--;
    }

    return text.substring(st, len);
}

protected String createPadding(int count) {
    StringBuilder padding = new StringBuilder();

    for (int i = 0; i < count; i++) {
        padding.append(' ');
    }

    return padding.toString();
}