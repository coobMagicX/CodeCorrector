private Node parseContextTypeExpression(JsDocToken token) {
    // Check if the token represents a function expression with 'new:' context
    if (token.getType() == JsDocTokenType.FUNCTION && token.getContext().equals("new:")) {
        // If so, return a constructor type instead of parsing the name as a nominal constructor
        return parseTypeName(token).getType(); // Assuming parseTypeName returns a TypeNode
    } else {
        // Otherwise, fall back to the original behavior
        return parseTypeName(token);
    }
}