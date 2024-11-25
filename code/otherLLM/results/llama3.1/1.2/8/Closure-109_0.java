private Node parseContextTypeExpression(JsDocToken token) {
    if (token.isStructuralConstructor()) {
        return parseClassType(token);
    } else {
        return parseTypeName(token);
    }
}