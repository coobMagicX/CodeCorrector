public String chompBalanced(char open, char close) {
    int start = -1;
    int end = -1;
    int depth = 0;
    char last = 0;
    boolean isEscaped = false; // This flag will track if the current character was escaped.

    do {
        if (isEmpty()) break;
        Character c = consume();
        if (last == ESC && !isEscaped) {
            isEscaped = true; // Set escape status if last character was an escape character
        } else {
            isEscaped = false; // Reset escape status if last character was not an escape character
        }

        if (!isEscaped) {
            if (c.equals(open)) {
                depth++;
                if (start == -1)
                    start = pos;
            }
            else if (c.equals(close)) {
                depth--;
            }
        }

        if (depth > 0 && last != 0)
            end = pos; // Don't include the outer match pair in the return
        last = c;
    } while (depth > 0);
    return (end >= 0) ? queue.substring(start, end) : "";
}