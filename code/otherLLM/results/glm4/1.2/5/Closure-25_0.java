private FlowScope traverseNew(Node n, FlowScope scope) {

  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  JSType constructorType = constructor.getJSType();

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
        // If constructorType is not a constructor, we cannot infer an instance type
        // from this constructor call. We should return the original scope.
        return scope;
      }
    }
  }

  // At this point, 'type' should be non-null if the constructor is valid.
  n.setJSType(type);

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  return scope;
}