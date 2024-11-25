private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  
  // Add type checking for referenced interfaces (extendedInterfaces)
  if (!compiler.getTypeSystem().contains(interfaceType)) {
    compiler.report(
        t.makeError(n, TYPE_NOT_FOUND, functionName, interfaceType.toString()));
    return;
  }
  
  Set<String> currentPropertyNames = implicitProto.getOwnPropertyNames();
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
    // Add recursive traversal with type checking
    if (!compiler.getTypeSystem().contains(iType)) {
      compiler.report(
          t.makeError(n, TYPE_NOT_FOUND, functionName, iType.toString()));
    } else {
      checkInterfaceConflictProperties(t, n, functionName, properties,
          currentProperties, iType);
    }
  }
}