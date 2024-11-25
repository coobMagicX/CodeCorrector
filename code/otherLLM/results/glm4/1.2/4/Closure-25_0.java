private FlowScope traverseNew(Node n, FlowScope scope) {

  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);

  JSType constructorType = constructor.getJSType();

  if (constructorType != null && constructorType.isConstructor()) {
    type = constructorType.getInstanceType();
  } else {
    constructorType = constructorType.restrictByNotNullOrUndefined();
    if (constructorType == null || constructorType.isUnknownType()) {
      type = getNativeType(UNKNOWN_TYPE);
    } else if (constructorType instanceof FunctionType) {
      FunctionType ct = (FunctionType) constructorType;
      if (ct.isConstructor()) {
        type = ct.getInstanceType();
      }
    }
  }

  n.setJSType(type);

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  return scope;
}