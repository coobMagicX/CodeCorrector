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
        text = text.substring(pos).trim(); // Trim before adding padding to avoid leading spaces issue
        text = padding + text; // Apply padding after trimming
        pos = findWrapPos(text, width, nextLineTabStop); // start looking for wrap position from nextLineTabStop

        if (pos == -1) {
            sb.append(text);
            return sb;
        }

        // Check if the wrap position is exactly at the end of the line
        // which may cause a wrap around at nextLineTabStop causing misalignment
        if ((text.length() > width) && (pos == nextLineTabStop - 1)) {
            pos = width;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}