private Node parseContextTypeExpression(JsDocToken token) {
    if (token.getType() == JsDocTokenType.STRUCTURAL_CONSTRUCTOR_FUNCTION) {
        return new Node(NodeType.STRUCTURAL_CONSTRUCTOR_FUNCTION, token.getValue());
    } else {
        return parseTypeName(token);
    }
}