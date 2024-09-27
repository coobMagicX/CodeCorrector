protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }

    String[] words = text.split("\\s+");
    int currentLineLength = 0;

    for (String word : words) {
        if (currentLineLength + word.length() > width) {
            sb.append(defaultNewLine);
            currentLineLength = 0;
        }

        sb.append(word).append(" ");
        currentLineLength += word.length() + 1;
    }

    return sb;
}