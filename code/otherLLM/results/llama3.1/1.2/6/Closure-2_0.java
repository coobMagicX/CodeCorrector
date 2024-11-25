private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  if (interfaceType == null || interfaceType.getImplicitPrototype() == null) {
    // Handle unknown interfaces
    return;
  }
  
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
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
  // Correct recursive calls
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    if (!iType.getImplicitPrototype().isProxy()) {
      checkInterfaceConflictProperties(t, n, functionName, properties,
          currentProperties, iType);
    }
  }
}