private ExtractionInfo extractMultilineTextualBlock(JsDocToken token, WhitespaceOption option) {
    if (token == JsDocToken.EOC || token == JsDocToken.EOL || token == JsDocToken.EOF) {
        return new ExtractionInfo("", token);
    }

    stream.update();
    int startLineno = stream.getLineno();
    int startCharno = stream.getCharno() + 1;

    StringBuilder builder = new StringBuilder();
    boolean ignoreStar = true;

    // Process tokens until the end of the comment is reached.
    while (true) {
        switch (token) {
            case STAR:
                // Only add ' ' or '*' if it's not the first token or if we are maintaining all spaces.
                if(ignoreStar) {
                    token = next();
                    ignoreStar = false; // Reset after potentially skipping a single star at the start of line
                } else {
                    if (builder.length() > 0) {
                        builder.append(" "); // Ensure space separation for '*'
                    }
                    builder.append('*');
                    token = next();
                }
                continue;

            case EOL:
                // Append newline and reset star-handling logic
                builder.append("\n");
                ignoreStar = true; // Each new line should reconsider whether to ignore '*' or not
                token = next();
                continue;

            case EOC:
            case EOF:
                // If the option is to trim, handle it before returning.
                String content = builder.toString();
                if (option != WhitespaceOption.PRESERVE) {
                    content = content.trim();
                }

                int endLineno = stream.getLineno();
                int endCharno = stream.getCharno();
                return new ExtractionInfo(content, token);

            default:
                // For any token which is not handling, we append it.
                ignoreStar = false; // Receiving any token apart from STAR after EOL means stop ignoring '*'
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(toString(token));
                String remainder = stream.getRemainingJSDocLine();
                if (option != WhitespaceOption.PRESERVE) {
                    remainder = trimEnd(remainder);
                }
                builder.append(remainder);
                
                token = next();
                break;
        }
    }
}

private String toString(JsDocToken token) {
    // This method should return the string representation of the JsDocToken,
    // likely involves checking the type of token and returning the correct form.
    // Example implementation:
    switch (token) {
        case ANNOTATION:
            return "@"; // Simplified example
        default:
            return token.toString();
    }
}

// Util method to trim end whitespace if needed, might be elsewhere in your class
private String trimEnd(String text) {
    return text.replaceAll("\\s+$", "");
}
