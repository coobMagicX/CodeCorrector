private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  // Check if the interfaceType is null or if it's proxying to a non-existent type.
  if (interfaceType == null || interfaceType.getImplicitPrototype() == null) {
    compiler.report(t.makeError(n, NULL_OR_NON_EXISTENT_INTERFACE_TYPE,
        functionName));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames = implicitProto.getOwnPropertyNames();

  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    
    // If there is no type defined for the property, report an error.
    if (oType == null) {
      compiler.report(t.makeError(n, NON_EXISTENT_PROPERTY_TYPE,
          functionName, name));
      continue;
    }
    
    if (!interfaceType.getPropertyType(name).isEquivalentTo(oType)) {
      compiler.report(
          t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
              functionName, name, oType.toString(),
              interfaceType.toString()));
    }

    currentProperties.put(name, interfaceType);
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}