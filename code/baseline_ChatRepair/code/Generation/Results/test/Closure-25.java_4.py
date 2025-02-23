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
      // The check and casting weren't capturing the correct type, improving this.
      if (ct == null && constructorType instanceof FunctionType) {
        ct = (FunctionType) constructorType;
      }
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
        if (type == null) {
          // Fallback if instance type missing, to unknown which should be safer than insecure null.
          type = getNativeType(UNKNOWN_TYPE);
        }
      } else {
        // This block handles edge cases where ct might not be a proper constructor.
        type = getNativeType(UNKNOWN_TYPE);
      }
    }
  }

  // Set the resolved type, falling back to unknown if none resolved.
  n.setJSType(type != null ? type : getNativeType(UNKNOWN_TYPE));
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  return scope;
}
