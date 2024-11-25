private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  try {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } catch (NullPointerException e) {
    // Handle proxy objects correctly by catching NullPointerException
    // and reporting a "Bad type annotation" error for each such extension.
    compiler.report(
        t.makeError(n, BAD_TYPE_ANNOTATION,
            functionName, interfaceType.toString()));
    return;
  }
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      if (!interfaceType.getPropertyType(name).isEquivalentTo(
          oType.getPropertyType(name))) {
        compiler.report(
            t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
                functionName, name, oType.toString(),
                interfaceType.toString()));
      }
    }
    currentProperties.put(name, interfaceType);
  }
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}