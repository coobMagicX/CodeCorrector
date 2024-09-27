private Node parseContextTypeExpression(JsDocToken token) {
    if (token != JsDocToken.STRING) {
        return reportGenericTypeSyntaxWarning();
    }
    
    Node typeNameNode = parseTypeName(token);
    skipEOLs();
    if (match(JsDocToken.LPAREN)) {
        next();
        Node parameters = parseParameters();
        typeNameNode.addChildToBack(parameters);
    }
    return typeNameNode;
}