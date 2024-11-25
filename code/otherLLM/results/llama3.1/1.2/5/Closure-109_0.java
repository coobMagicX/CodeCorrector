private Node parseContextTypeExpression(JsDocToken token) {
    if (token == JsDocToken.NEW || token == JsDocToken.CLASS) {
        // Add logic to handle context type expressions specifically
        Node constructorName = parseTypeName(token);
        return new Node(Node.CONSTRUCTOR_TYPE_NODE, constructorName);
    } else {
        // Simply call another method without any additional processing might be the root cause of the problem
        return parseTypeName(token);
    }
}