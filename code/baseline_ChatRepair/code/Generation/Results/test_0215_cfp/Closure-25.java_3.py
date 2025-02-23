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
      if (ct == null && constructorType.isFunctionType()) {
        // Ensuring that any function-like type can be treated as a constructor if applicable.
        ct = (FunctionType) constructorType;
      }
      if (ct != null) {
        if (ct.isConstructor() || ct.isInterface()) { // Ensure we consider interfaces as potential constructors for type inferences.
          type = ct.getInstanceType();
        } else {
          // If it's not a constructor, check if there is a default instance type provided for objects of this kind.
          type = getNativeType(OBJECT_TYPE);
        }
      }
    }
  }

  n.setJSType(type != null ? type : getNativeType(UNKNOWN_TYPE)); // Ensure types never incorrectly go set to null.
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  return scope;
}
