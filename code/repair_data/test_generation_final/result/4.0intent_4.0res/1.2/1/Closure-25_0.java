private FlowScope traverseNew(Node n, FlowScope scope) {
    Node constructor = n.getFirstChild();
    scope = traverse(constructor, scope);
    JSType constructorType = constructor.getJSType();
    JSType type = null;
    if (constructorType != null) {
        constructorType = constructorType.restrictByNotNullOrUndefined();
        if (constructorType.isUnknownType()) {
            type = getNativeType(UNKNOWN_TYPE);
        } else {
            FunctionType ct = constructorType.toMaybeFunctionType();
            if (ct == null && constructorType instanceof FunctionType) {
                // If constructorType is a NoObjectType, then toMaybeFunctionType will
                // return null. But NoObjectType implements the FunctionType
                // interface, precisely because it can validly construct objects.
                ct = (FunctionType) constructorType;
            }
            if (ct != null && ct.isConstructor()) {
                type = ct.getInstanceType();
            }
        }
    }
    n.setJSType(type);

    // Update: Traverse and possibly update argument types based on constructor parameter types
    Node arg = constructor.getNext();
    if (ct != null && ct.isConstructor() && ct.getParametersNode() != null) {
        Node param = ct.getParametersNode().getFirstChild();
        while (arg != null && param != null) {
            scope = traverse(arg, scope);
            // Check if the argument type should be updated to the parameter type
            JSType argType = arg.getJSType();
            JSType paramType = param.getJSType();
            if (argType != paramType) {
                arg.setJSType(paramType);
            }
            arg = arg.getNext();
            param = param.getNext();
        }
    } else {
        while (arg != null) {
            scope = traverse(arg, scope);
            arg = arg.getNext();
        }
    }
    return scope;
}