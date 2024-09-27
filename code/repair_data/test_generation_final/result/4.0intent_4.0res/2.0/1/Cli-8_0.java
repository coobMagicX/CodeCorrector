protected StringBuffer renderWrappedText(StringBuffer sb, int width, 
                                         int nextLineTabStop, String text) {
    int pos = findWrapPos(text, width, 0);

    if (pos == -1) {
        sb.append(rtrim(text));
        return sb;
    }
    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    // Prepare the initial text for subsequent lines
    text = text.substring(pos).trim(); 

    // all following lines must be padded with nextLineTabStop space characters
    final String padding = createPadding(nextLineTabStop);

    while (!text.isEmpty()) {
        text = padding + text; // Apply padding at the beginning of each new line
        pos = findWrapPos(text, width, 0); // Check for wrap position in the padded text

        if (pos == -1 || pos >= text.length()) {
            sb.append(text); // Append the rest of the text if no more wrapping is needed
            return sb;
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
        text = text.substring(pos).trim(); // Update text to remaining part after last wrap
    }
    return sb;
}