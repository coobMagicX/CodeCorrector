private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  if (interfaceType == null) {
    compiler.report(t.makeError(n, INVALID_INTERFACE_TYPE, functionName, "interfaceType", "null"));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  
  // Check for non-existent implicit prototype
  if (implicitProto == null) {
    compiler.report(t.makeError(n, NON_EXISTENT_IMPLICIT_PROTO, functionName, "interfaceType", interfaceType.toString()));
    return;
  }
  
  currentPropertyNames = implicitProto.getOwnPropertyNames();
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      if (!interfaceType.getPropertyType(name).isEquivalentTo(oType.getPropertyType(name))) {
        compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
            functionName, name, oType.toString(), interfaceType.toString()));
      }
    } else {
      // Handle the case where the property does not exist in the properties map
      compiler.report(t.makeError(n, UNKNOWN_PROPERTY_TYPE, functionName, name));
    }
    currentProperties.put(name, interfaceType);
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }
}