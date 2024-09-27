private Node parseContextTypeExpression(JsDocToken token) {
    if (token == JsDocToken.QMARK) {
        return new Node(Token.QMARK);
    } else {
        return parseBasicTypeExpression(token);
    }
}