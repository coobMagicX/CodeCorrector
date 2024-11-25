private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  // Check if the interface exists before proceeding
  if (interfaceType == null || !interfaceExists(interfaceType)) {
    compiler.report(t.makeError(n, NON_EXISTENT_INTERFACE, functionName));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;

  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  if (implicitProto != null) {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } else {
    currentPropertyNames = Collections.emptySet(); // If prototype is null, handle gracefully
  }

  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      ObjectType iType = interfaceType.getPropertyType(name);
      if (!iType.isEquivalentTo(oType)) {
        compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
            functionName, name, oType.toString(), iType.toString()));
      }
    }
    currentProperties.put(name, interfaceType);
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }
}

// Helper method to check if an interface exists
private boolean interfaceExists(ObjectType type) {
  // This method should contain logic to verify the existence of the interface.
  // Implementation depends on the specific details of the system this code is part of.
  // For example:
  // return someInterfaceRegistry.contains(type.getName());
  return true; // Placeholder, replace with actual implementation
}