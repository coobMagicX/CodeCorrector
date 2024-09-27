private Node parseContextTypeExpression(JsDocToken token) {
    if (token == JsDocToken.QMARK) {
        return new Token(Token.QMARK);
    } else {
        return parseBasicTypeExpression(token);
    }
}