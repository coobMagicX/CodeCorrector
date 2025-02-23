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
        // Attempt to get function constructor if ct is not directly castable to FunctionType
        ct = ((ObjectType) constructorType).getConstructor();
      }
      if (ct != null) {
        if (ct.isConstructor()) {
          type = ct.getInstanceType();
          // Inference might be necessary if there are parameters
          type = inferTypeFromArguments(ct, n);
        } else if (ct.isInterface()) {
          type = registry.getNativeType(UNKNOWN_TYPE);
        }
      }
    }
  }
  n.setJSType(type);

  // Continue to traverse through the arguments
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  
  return scope;
}

private JSType inferTypeFromArguments(FunctionType ctorType, Node newNode) {
  // Simple example: adjust the instance type considering the arguments
  JSType instanceType = ctorType.getInstanceType();
  Node argNode = newNode.getFirstChild().getNext();
  while (argNode != null) {
    // Considerations of argument types can be added here
    if (argNode.getJSType() != null && instanceType.hasProperty("foo")) {
      instanceType = instanceType.findPropertyType("foo").getLeastSupertype(argNode.getJSType());
    }
    argNode = argNode.getNext();
  }
  return instanceType;
}
