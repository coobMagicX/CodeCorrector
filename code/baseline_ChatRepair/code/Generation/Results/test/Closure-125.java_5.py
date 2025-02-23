private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

    // Directly exit if the necessary type is not acquired or it is not a constructor
    if (!type.isConstructor()) {
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
        return;
    }

    // Since it must be a constructor at this point, we can safely cast to FunctionType
    FunctionType fnType = (FunctionType) type;
    visitParameterList(t, n, fnType);
    ensureTyped(t, n, fnType.getInstanceType());
}
