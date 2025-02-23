private FlowScope traverseNew(Node n, FlowScope scope) {
  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  JSType constructorType = constructor.getJSType();
  JSType type = null;

  if (constructorType != null) {
    constructorType = constructorType.restrictByNotNullOrUndefined();
    if (constructorType.isUnknownType()) {
      type = getNativeType(UNKNOWN_TYPE); // Default to unknown type if the constructor's type is unknown.
    } else {
      FunctionType ct = constructorType.toMaybeFunctionType();
      if (ct == null && constructorType instanceof FunctionType) {
        ct = (FunctionType) constructorType;
      }
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
        // Introduction of a refinement for backward function compatibility
        type = inferInstanceType(constructor, type);
      }
    }
  } else {
    type = getNativeType(UNKNOWN_TYPE); // Ensure type is unknown if no constructor type is inferred
  }

  n.setJSType(type);
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  return scope;
}

private JSType inferInstanceType(Node constructor, JSType instanceType) {
  // This placeholder method represents an approach to refine the instance type
  // based on arguments and other details. This differs per application context and needs.
  return instanceType; // Modify according to specific rules and context.
}
