private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // Add prevalidation to ensure the interface exists before checking conflicts
  if (!implicitProto.isFunction()) {
    compiler.reportMissingProperties(true).report(t.makeError(n, INCOMPATIBLE_EXTENDED_INTERFACE_TYPE, functionName, interfaceType.toString()));
    return; // Exit early if the interface does not exist
  }
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
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}