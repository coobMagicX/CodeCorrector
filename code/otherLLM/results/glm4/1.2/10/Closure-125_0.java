private void visitNew(NodeTraversal t, Node n) {
    // Retrieve the first child of the node to find the constructor
    Node constructor = n.getFirstChild();

    // Get the JSType for the constructor node and restrict it to not-null or undefined
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

    // Check if the type is a constructor, empty type, or unknown type
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        
        // If the node is a function type, visit its parameter list and ensure it's typed
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            // Ensure that the instance type of the function type is correctly typed
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            // If not a function type but is constructor, empty or unknown type, just ensure typing without providing an instance type
            ensureTyped(t, n);
        }
    } else {
        // Report that this node is not a constructor and ensure it's typed
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
    }
}