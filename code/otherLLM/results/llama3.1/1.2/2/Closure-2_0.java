private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  currentPropertyNames = implicitProto.getOwnPropertyNames();
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      try {
        if (!interfaceType.getPropertyType(name).isEquivalentTo(
            oType.getPropertyType(name))) {
          compiler.report(
              t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
                  functionName, name, oType.toString(),
                  interfaceType.toString()));
        }
      } catch (NoSuchPropertyException e) {
        // Handle the case where the property type is unknown
        compiler.report(
            t.makeError(n, UNKNOWN_PROPERTY_TYPE, functionName, name));
      }
    }
    currentProperties.put(name, interfaceType);
  }
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}