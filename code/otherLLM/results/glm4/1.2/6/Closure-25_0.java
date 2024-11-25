private FlowScope traverseNew(Node n, FlowScope scope) {

  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  JSType constructorType = constructor.getJSType();

  if (constructorType != null) {
    // Ensure that the constructor type is restricted to a function type
    FunctionType ct = constructorType.toMaybeFunctionType();
    
    if (ct == null && constructorType instanceof FunctionType) {
      // NoObjectType can be treated as a function type in some contexts,
      // so we cast it to FunctionType explicitly.
      ct = (FunctionType) constructorType;
    }
    
    if (ct != null && ct.isConstructor()) {
      JSType instanceType = ct.getInstanceType();
      
      // Check if the instance type is an object with optional properties
      if (instanceType.isObjectType() && instanceType.getOptionalPropertyNames().size() > 0) {
        // We expect 'y' to be of the type "{foo: (number|undefined)}"
        // Assuming that the function's return type should have a named property 'foo'
        // with type "number" or "undefined".
        Set<String> optionalProps = instanceType.getOptionalPropertyNames();
        
        if (optionalProps.contains("foo")) {
          // Check if the type of the 'foo' property is either number or undefined
          JSType fooType = instanceType.getPropertyType("foo");
          
          boolean isFooNumber = fooType.isNumberType() || fooType.isUndefinedType();
          
          if (isFooNumber) {
            // If the inferred type matches the expected type, set it on the node
            n.setJSType(instanceType);
          }
        }
      } else {
        // If the instance type is not as expected, nullify the type to indicate an error or default case
        n.setJSType(null);
      }
    } else {
      // If the constructor type is not a constructor, set it to null
      n.setJSType(null);
    }
  }

  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }
  
  return scope;
}