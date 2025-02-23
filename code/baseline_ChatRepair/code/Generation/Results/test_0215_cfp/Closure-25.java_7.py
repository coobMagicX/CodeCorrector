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
        ct = (FunctionType) constructorType;
      }
      // Check if ct is a constructor and assign the instance type
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
      }
      // If ct is null or not a constructor, use ANY_TYPE
      else {
        type = getNativeType(ANY_TYPE);
      }
    }
  }
  
  // As a fallback, use UNKNOWN_TYPE when type cannot be determined.
  if (type == null) {
    type = getNativeType(UNKNOWN_TYPE);
  }
  
  n.setJSType(type);

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  return scope;
}
