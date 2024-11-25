private FlowScope traverseNew(Node n, FlowScope scope) {

  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  JSType constructorType = constructor.getJSType();
  JSType type = null;

  // Ensure the correct type is inferred for the object literal passed to the constructor
  if (constructorType != null) {
    constructorType = constructorType.restrictByNotNullOrUndefined();

    if (constructorType.isUnknownType()) {
      type = getNativeType(UNKNOWN_TYPE);
    } else {
      FunctionType ct = constructorType.toMaybeFunctionType();
      // Check for NoObjectType before casting to FunctionType
      if (ct == null && constructorType instanceof NoObjectType) {
        ct = (FunctionType) constructorType;
      }
      
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
      }
    }
  }

  n.setJSType(type); // Set the inferred type on the node

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  return scope;
}