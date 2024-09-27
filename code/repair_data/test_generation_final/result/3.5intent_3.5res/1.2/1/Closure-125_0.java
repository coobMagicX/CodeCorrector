private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

    // Check if the type can actually create instances, considering constructors and interfaces.
    if (type.isConstructor()) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            // Ensure typing even if it's not a constructable function type
            ensureTyped(t, n);
        }
    } else if (type.isInterface()) {
        // Direct instantiation of interfaces should be reported as not a constructor
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
    } else if (type.isEmptyType() || type.isUnknownType()) {
        // For empty or unknown types, simply ensure typing without further checks
        ensureTyped(t, n);
    } else {
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
    }
}

