private Node parseContextTypeExpression(JsDocToken token) {
    Node typeExpr = parseTypeName(token);
    while ((token = next()) != JsDocToken.EOF && token != JsDocToken.EOC) {
       