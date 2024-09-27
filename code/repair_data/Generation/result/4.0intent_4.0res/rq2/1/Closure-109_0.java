private Node parseContextTypeExpression(JsDocToken token) {
    if (token != JsDocToken.STRING) {
        return reportGenericTypeSyntaxWarning();
    }
    return parseTypeName(token);
}