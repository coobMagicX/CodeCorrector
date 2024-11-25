private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  // Check if the interfaceType is valid to prevent errors when accessing methods on it.
  if (interfaceType == null || !interfaceType.isValid()) {
    compiler.report(t.makeError(n, INTERFACE_TYPE_IS_NULL_OR_INVALID, functionName));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  if (implicitProto != null) {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } else {
    compiler.report(t.makeError(n, NON_EXISTENT_IMPLICIT_PROTOTYPE, functionName));
    return;
  }

  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      if (!interfaceType.getPropertyType(name).isEquivalentTo(
          oType.getPropertyType(name))) {
        compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
            functionName, name, oType.toString(), interfaceType.toString()));
      }
    }
    currentProperties.put(name, interfaceType);
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }
}

// Error code constants
private static final String INTERFACE_TYPE_IS_NULL_OR_INVALID = "INTERFACE_TYPE_IS_NULL_OR_INVALID";
private static final String NON_EXISTENT_IMPLICIT_PROTOTYPE = "NON_EXISTENT_IMPLICIT_PROTOTYPE";