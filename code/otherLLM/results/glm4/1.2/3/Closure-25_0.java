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
      
      if (ct == null && constructorType instanceof NoObjectType) {
        // If constructorType is a NoObjectType, it can be considered a function
        // type for object construction purposes.
        ct = new NoObjectFunctionType((NoObjectType)constructorType);
      }
      
      if (ct != null && ct.isConstructor()) {
        type = ct.getInstanceType();
      } else if (constructorType instanceof FunctionType) {
        // Directly cast to FunctionType if constructorType is already of that type.
        ct = (FunctionType) constructorType;
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

// Helper class to handle NoObjectType as a function type
private static class NoObjectFunctionType extends FunctionType {
  private final NoObjectType noObject;

  public NoObjectFunctionType(NoObjectType noObject) {
    super(NO_FUNCTION_TYPE, new ParameterList(), null, null);
    this.noObject = noObject;
  }

  @Override
  public JSType getInstanceType() {
    // As we are treating it like a function that constructs an object,
    // the instance type would be the NoObjectType itself.
    return noObject;
  }
}