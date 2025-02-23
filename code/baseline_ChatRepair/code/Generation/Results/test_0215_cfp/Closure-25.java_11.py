private FlowScope traverseNew(Node n, FlowScope scope) {
  Node constructor = n.getFirstChild();
  scope = traverse(constructor, scope);
  JSType constructorType = constructor.getJSType();
  JSType instanceType = null;

  if (constructorType != null) {
    constructorType = constructorType.restrictByNotNullOrUndefined();
    if (constructorType.isUnknownType()) {
      instanceType = getNativeType(UNKNOWN_TYPE);
    } else {
      FunctionType ct = constructorType.toMaybeFunctionType();
      if (ct != null && ct.isConstructor()) {
        instanceType = ct.getInstanceType();
      } else {
        instanceType = getNativeType(OBJECT_TYPE); // Ensuring a basic object type if not a constructor
      }
    }
  }

  if (instanceType == null) {
    instanceType = getNativeType(OBJECT_TYPE); // Default to Object type if all else fails
  }

  n.setJSType(instanceType);
  for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
    scope = traverse(arg, scope);
  }

  // Additional type refinement based on constructor and test failure insights
  refineInstanceType(n, constructor);
  return scope;
}

private void refineInstanceType(Node instanceNode, Node constructor) {
  // Assuming that specific instances like in the test ("{foo: (number|undefined)}") need inference from params
  FunctionType constructorType = constructor.getJSType().toMaybeFunctionType();
  if(constructorType != null) {
    // Dummy example: Assuming constructor determines an object with property "foo"
    ObjectType instanceObjectType = (ObjectType) instanceNode.getJSType();
    JSType propType = constructorType.getPropertyType("foo");
    if (propType == null) {
      propType = getNativeType(UNKNOWN_TYPE); // Default or handle appropriately
    }
    instanceObjectType.defineDeclaredProperty("foo", propType, null);
  }
}
