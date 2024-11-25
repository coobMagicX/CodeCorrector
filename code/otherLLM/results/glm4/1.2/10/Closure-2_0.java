private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  // Check for non-existent interface before accessing its prototype
  if (interfaceType == null || interfaceType.getImplicitPrototype() == null) {
    compiler.report(
        t.makeError(n, NON_EXISTENT_INTERFACE_ERROR,
            functionName));
    return;
  }

  Set<String> currentPropertyNames = new HashSet<>(interfaceType.getOwnPropertyNames());
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      // Check property types and report error if they are not equivalent
      if (!interfaceType.getPropertyType(name).isEquivalentTo(oType.getPropertyType(name))) {
        compiler.report(
            t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
                functionName, name, oType.toString(),
                interfaceType.toString()));
      }
    } else {
      // Handle the case where the property is not found in the properties map
      currentProperties.put(name, interfaceType);
    }
  }

  // Update the currentProperties with properties from extended interfaces recursively
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        new HashMap<>(currentProperties), iType);
  }
}