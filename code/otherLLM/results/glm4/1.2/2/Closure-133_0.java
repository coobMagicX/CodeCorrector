private String getRemainingJSDocLine() {
    StringBuilder remaining = new StringBuilder();
    JsDocToken token = stream.peek(); // Use peek instead of next() if you want to avoid consuming the tokens

    while (token != null && !isAtEnd(token)) {
        if (token.getType() == JsDocToken.STRING) {
            // Handle string tokens
            String content = stream.getString();
            remaining.append(content);
        } else if (token.getType() == JsDocToken.COLON || token.getType() == JsDocToken.BRACKET) {
            // Consume the colon or bracket and append the next token's content
            consume();
            token = stream.peek(); // Move to the next token after consuming a colon or bracket
        } else if (token.getType() != JsDocToken.COMMENT && token.getType() != JsDocToken.WHITE_SPACE) {
            // If it's not a comment or whitespace, let parseResultType handle it
            Node typeNode = parseResultType(token);
            remaining.append(typeNode.toString()); // Assuming toString() can provide a representation of the node
        }
        token = stream.peek(); // Move to the next token
    }

    return remaining.toString();
}

private boolean isAtEnd(JsDocToken token) {
    return token == null || (token.getType() == JsDocToken.EOF);
}