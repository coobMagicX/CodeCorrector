private Node parseContextTypeExpression(JsDocToken token) {
    // Check if the token is a constructor by looking at its type
    if (token == JsDocToken.CONSTRUCTOR) {
        // If it's a constructor, directly report the syntax warning and return null
        return reportSyntaxWarning("msg.jsdoc.constructor", token);
    }

    // If not, fall back to parsing the type name as usual
    return parseTypeName(token);
}