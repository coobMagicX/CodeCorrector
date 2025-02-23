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
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
      } else {
        // Use UNKNOWN_TYPE as a fallback if no suitable type can be determined.
        type = getNativeType(UNKNOWN_TYPE);
      }
    }
  }
  
  // Ensure the node is never without a type, use UNKNOWN_TYPE as default.
  if (type == null) {
    type = getNativeType(UNKNOWN_TYPE);
  }
  
  n.setJSType(type);

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  return scope;
}
