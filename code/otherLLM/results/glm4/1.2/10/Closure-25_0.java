private FlowScope traverseNew(Node n, FlowScope scope) {

  Node constructor = n.getFirstChild();
  if (constructor == null) {
    // Handle the case where there is no constructor node.
    n.setJSType(getNativeType(UNKNOWN_TYPE));
  } else {
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
          // return null. But NoObjectType implements the FunctionType interface,
          // precisely because it can validly construct objects.
          ct = (FunctionType) constructorType;
        }
        
        if (ct != null && ct.isConstructor()) {
          type = ct.getInstanceType();
        } else {
          // If 'type' is still null after checking for a constructor, then we
          // might need to set the type explicitly to something other than null.
          type = getNativeType(UNKNOWN_TYPE);
        }
      }
    }

    n.setJSType(type); // Ensure that the node's JSType is updated with the inferred or default type.

    for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
      scope = traverse(arg, scope);
    }
  }
  
  return scope;
}