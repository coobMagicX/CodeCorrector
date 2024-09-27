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
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  // Handle type inference for the newly created object
  if (n.isNew()) {
    Node parent = n.getParent();
    if (parent != null && parent.isAssign()) {
      Node lhs = parent.getFirstChild();
      if (lhs.isName()) {
        String name = lhs.getString();
        JSType inferredType = getInferredType(n);
        if (inferredType != null) {
          scope = scope.inferSlotType(name, inferredType, false);
        }
      }
    }
  }
  return scope;
}