private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;

  // Check if implicitProto is null before accessing its properties
  if (implicitProto != null) {
    currentPropertyNames = implicitProto.getOwnPropertyNames();

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
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}
