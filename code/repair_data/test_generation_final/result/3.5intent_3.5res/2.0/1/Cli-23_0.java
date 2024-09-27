protected StringBuffer renderWrappedText(StringBuffer sb, int width, 
                                         int nextLineTabStop, String text)
{
    int pos = findWrapPos(text, width, 0);

    if (pos == -1)
    {
        sb.append(rtrim(text));

        return sb;
    }
    sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);

    // all following lines must be padded with nextLineTabStop space 
    // characters
    final String padding = createPadding(nextLineTabStop);

    int count = 0; // Added count variable

    while (true)
    {
        int lastPos = pos;
        text = padding + text.substring(pos).trim();
        pos = findWrapPos(text, width, 0);

        if (pos == -1)
        {
            sb.append(text);

            return sb;
        } else if (pos == lastPos)
        {
            if (count > 0) { // Check if count is greater than 0
                throw new RuntimeException("Text too long for line - throwing exception to avoid infinite loop [CLI-162]: " + text);
            }
            count++; // Increment count
        }

        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
    }
}