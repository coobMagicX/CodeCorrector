private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    
    // Check for constructor call or declaration with empty or unknown types.
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();

        if (fnType != null) {
            visitParameterList(t, n, fnType);
            
            // Ensure the instance type is correctly typed for the function.
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            // If not a constructor but has an empty or unknown type, report it.
            if (type.isEmptyType() || type.isUnknownType()) {
                report(t, n, UNKNOWN_TYPE);
            }
            ensureTyped(t, n); // Ensure typing regardless of whether it's a constructor or not.
        }
    } else {
        // If the node is not a constructor but has another type, report it.
        if (!type.isConstructor() && !type.isEmptyType() && !type.isUnknownType()) {
            report(t, n, NOT_A_CONSTRUCTOR);
        }
        ensureTyped(t, n); // Ensure typing regardless of whether it's a constructor or not.
    }
}