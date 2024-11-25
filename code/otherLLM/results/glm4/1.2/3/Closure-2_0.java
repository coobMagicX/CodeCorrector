private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  if (interfaceType == null) { // Check for non-existent interface type
    compiler.report(
        t.makeError(n, INEXISTENT_INTERFACE_TYPE, functionName));
    return;
  }
  
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  if (implicitProto != null) {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } else {
    // Handle the case where there is no implicit prototype
    currentPropertyNames = Collections.emptySet();
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
    } else {
      // If the property is not in properties map, it can be reported as a missing property
      checkPropertyAccessHelper(interfaceType, name, t, n);
    }
    currentProperties.put(name, interfaceType);
  }
  
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
}