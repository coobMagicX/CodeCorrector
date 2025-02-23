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
      if (ct == null && constructorType instanceof ObjectType) {
        // Fallback if it's an object type that might behave like a function
        ct = ((ObjectType) constructorType).toMaybeFunctionType();
      }
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
      }
    }
  }
  n.setJSType(type);

  // Traverse the arguments
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  return scope;
}
