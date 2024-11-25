private Node parseContextTypeExpression(JsDocToken token) {
    if (token == JsDocToken.CONTEXT_TYPE_EXPRESSION) {
        return parseTypeName(token);
    } else {
        // Add a check to handle structural constructors
        if (token == JsDocToken.STRUCTURAL_CONSTRUCTOR || token == JsDocToken.NOMINAL_CONSTRUCTOR) {
            return reportStructuralConstructorSyntaxWarning();
        }
        return reportTypeSyntaxWarning("msg.jsdoc.invalid.context.type");
    }
}