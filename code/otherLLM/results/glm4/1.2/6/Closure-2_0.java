private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  if (interfaceType == null) {
    compiler.report(t.makeError(n, UNKNOWN_INTERFACE_TYPE, functionName));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  if (implicitProto != null) {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } else {
    compiler.report(t.makeError(n, NON_EXISTENT_PARENT_INTERFACE, functionName));
    return;
  }

  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType == null) {
      // If the property doesn't exist in properties map, it might be new,
      // so we don't report an error here.
      continue;
    }
    if (!interfaceType.getPropertyType(name).isEquivalentTo(
        oType.getPropertyType(name))) {
      compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
          functionName, name, oType.toString(), interfaceType.toString()));
    }
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }

  // Populate the currentProperties map with the properties of the interface
  // so they can be checked in the recursive call.
  currentPropertyNames.forEach(name -> currentProperties.put(name, interfaceType));
}