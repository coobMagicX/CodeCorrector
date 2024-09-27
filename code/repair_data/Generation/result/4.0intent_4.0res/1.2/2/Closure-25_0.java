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
    
    // Update the scope and potentially the types of arguments based on the constructor parameters
    Node arg = constructor.getNext();
    if (ct != null && ct.isConstructor()) {
        int paramIndex = 0;
        while (arg != null) {
            JSType argType = arg.getJSType();
            NodeList<Node> parameters = ct.getParametersNode();
            if (paramIndex < parameters.size()) {
                Node param = parameters.get(paramIndex);
                JSType paramType = param.getJSType();
                if (argType != null && !argType.isEquivalentTo(paramType)) {
                    arg.setJSType(paramType);
                }
            }
            scope = traverse(arg, scope);
            arg = arg.getNext();
            paramIndex++;
        }
    } else {
        while (arg != null) {
            scope = traverse(arg, scope);
            arg = arg.getNext();
        }
    }
    return scope;
}