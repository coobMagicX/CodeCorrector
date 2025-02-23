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
      if (ct != null) {
        if (ct.isConstructor()) {
          type = ct.getInstanceType();
        } else {
          type = getNativeType(OBJECT_TYPE); // Always ensure some type is set, preferring Object type
        }
      } else {
        // As last resort, infer as generic OBJECT_TYPE
        type = getNativeType(OBJECT_TYPE);
      }
    }
  }
  
  // As a fallback, ensure the type is not left unset.
  if (type == null) {
    type = getNativeType(UNKNOWN_TYPE);
  }
  
  n.setJSType(type);

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  
  return scope;
}
