protected int findWrapPos(String text, int width, int startPos)
{
    int pos;
    
    // the line ends before the max wrap pos or a new line char found
    if (((pos = text.indexOf('\n', startPos)) != -1 && pos <= width)
            || ((pos = text.indexOf('\t', startPos)) != -1 && pos <= width))
    {
        return pos + 1;
    }
    else if (startPos + width >= text.length())
    {
        return -1;
    }

    // look for the last whitespace character before startPos+width
    pos = startPos + width;

    char c;

    while ((pos >= startPos) && ((c = text.charAt(pos)) != ' ')
            && (c != '\n') && (c != '\r'))
    {
        --pos;
    }

    // if we found it - just return
    if (pos > startPos)
    {
        return pos;
    }
    
    // if we didn't find one, simply chop at startPos+width
    pos = startPos + width;
    while ((pos <= text.length()) && ((c = text.charAt(pos)) != ' ')
           && (c != '\n') && (c != '\r'))
    {
        ++pos;
    }        
    return pos == text.length() ? -1 : pos;
}

protected String rtrim(String s)
{
    if ((s == null) || (s.length() == 0))
    {
        return s;
    }

    int pos = s.length();

    while ((pos > 0) && Character.isWhitespace(s.charAt(pos - 1)))
    {
        --pos;
    }

    return s.substring(0, pos);
}

protected int findWrapPos(String text, int width, int startPos)
{
    int pos;
    
    // the line ends before the max wrap pos or a new line char found
    if (((pos = text.indexOf('\n', startPos)) != -1 && pos <= width)
            || ((pos = text.indexOf('\t', startPos)) != -1 && pos <= width))
    {
        return pos + 1;
    }
    else if (startPos + width >= text.length())
    {
        return -1;
    }

    // look for the last whitespace character before startPos+width
    pos = startPos + width;

    char c;

    while ((pos >= startPos) && ((c = text.charAt(pos)) != ' ')
            && (c != '\n') && (c != '\r'))
    {
        --pos;
    }
    
    // if we didn't find one, try to find the next whitespace character after startPos+width
    if (pos == startPos)
    {
        pos = startPos + width;
        while ((pos <= text.length()) && ((c = text.charAt(pos)) != ' ')
               && (c != '\n') && (c != '\r'))
        {
            ++pos;
        }
    }

    return pos == text.length() ? -1 : pos;
}