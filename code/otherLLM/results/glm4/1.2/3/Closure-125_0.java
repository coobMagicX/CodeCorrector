private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

    // Step 1: Verify the JSType of the function being called matches expected types.
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        
        // Step 2: Confirm that the type resolution logic correctly handles cases where a function is being used as a constructor.
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            
            // Step 3: Ensure that the `ensureTyped` method is invoked with the correct type information when needed.
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            // Step 4: Check for proper handling of undefined or empty types in the context of constructor calls.
            report(t, n, EMPTY_OR_UNDEFINED_TYPE);
        }
    } else {
        // Step 5: Validate the reporting mechanism for non-constructor types and ensure it triggers appropriately.
        if (!type.isConstructor()) {
            report(t, n, NOT_A_CONSTRUCTOR);
        }
        
        // Ensure `ensureTyped` is still called regardless of type
        ensureTyped(t, n);
    }
}