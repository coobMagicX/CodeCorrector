private Node parseContextTypeExpression(JsDocToken token) {
    JsDocToken typeToken = getNextExpectedTokenType(token, JSDOC_TOKEN_TYPE_CONTEXT_TYPE);
    if (typeToken == null || !token.type.equals(typeToken.type)) {
        // Handle unexpected tokens by throwing an exception or logging a warning
        throw new JsDocParseException("Unexpected token in context type expression");
    }

    return parseTypeName(typeToken);
}

// Helper method to get the next expected token type
private JsDocToken getNextExpectedTokenType(JsDocToken token, String expectedTokenType) {
    while (token != null) {
        if (token.type.equals(expectedTokenType)) {
            return token;
        }
        token = getTokenNext(token);
    }
    return null;
}