private FlowScope traverseNew(Node n, FlowScope scope) {
  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  
  JSType constructorType = constructor.getJSType();
  if (constructorType != null) {
    // Restrict the type to not be null or undefined before proceeding
    constructorType = constructorType.restrictByNotNullOrUndefined();
    
    if (constructorType.isUnknownType()) {
      // If it's an unknown type, use the native type for unknown types
      type = getNativeType(UNKNOWN_TYPE);
    } else {
      FunctionType ct = constructorType.toMaybeFunctionType();
      
      if (ct == null && constructorType instanceof FunctionType) {
        // If constructorType is a NoObjectType, cast it to FunctionType since it can construct objects
        ct = (FunctionType) constructorType;
      }
      
      if (ct != null && ct.isConstructor()) {
        // If the type represents a constructor, use its instance type
        type = ct.getInstanceType();
      }
    }
  }

  // Set the inferred type on the node to ensure it's correctly recorded
  n.setJSType(type);
  
  // Traverse arguments if there are any
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  
  return scope;
}