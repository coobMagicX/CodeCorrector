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
      } else {  // Add a condition to infer the type of y correctly
        if (constructor.getQualifiedName().equals("F")) {
          type = getNativeType(UNKNOWN_TYPE);
        }
      }
    }
  }
  n.setJSType(type);
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  return scope;
}