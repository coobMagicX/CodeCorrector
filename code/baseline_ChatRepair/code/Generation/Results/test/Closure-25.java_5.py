private FlowScope traverseNew(Node n, FlowScope scope) {
    Node constructor = n.getFirstChild();
    scope = traverse(constructor, scope);
    JSType constructorType = constructor.getJSType();
    JSType type = null;

    if (constructorType != null) {
        constructorType = constructorType.restrictByNotNullOrUndefined();
        FunctionType ct = constructorType.toMaybeFunctionType();
        if (ct != null && (ct.isConstructor() || ct.isInterface())) {
            // Use instance type if it's a valid constructor or an interface which can generate types.
            type = ct.getInstanceType();
        } else {
            // Otherwise, trying to fetch type information might mislead the inference, stick to constructor type.
            type = constructorType;
        }
    }

    // If type couldn't be determined, default to unknown type.
    if (type == null) {
        type = getNativeType(UNKNOWN_TYPE);
    }

    n.setJSType(type);
    
    // Traverse remaining siblings (arguments to the "new" constructor).
    for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
        scope = traverse(arg, scope);
    }
    return scope;
}
