private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    
    // Check if the first child is a FunctionExpression or FunctionDeclaration node.
    boolean isConstructorFunction = constructor != null && (constructor.getType() == TokenType.FUNCTION_EXPRESSION || constructor.getType() == TokenType.FUNCTION_DECLARATION);
    
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    if (isConstructorFunction && (type.isConstructor() || type.isEmptyType() || type.isUnknownType())) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            ensureTyped(t, n); // This case can occur when the function has no return type or is anonymous.
        }
    } else if (!isConstructorFunction) {
        report(t, n, NOT_A_CONSTRUCTOR);
    } else {
        report(t, n, NOT_A_FUNCTION_TYPE);
        ensureTyped(t, n);
    }
}