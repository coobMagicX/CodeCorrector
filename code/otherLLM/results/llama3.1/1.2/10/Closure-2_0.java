private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  try {
    // Validate type annotation for extended interfaces
    Class<?> clazz = Class.forName(interfaceType.getClassName());
    if (clazz != null && !interfaceType.isProxy()) {
      currentPropertyNames = clazz.getMethods().stream()
          .map(Method::getName)
          .collect(Collectors.toSet());
    } else {
      // Error handling for unknown types
      compiler.report(t.makeError(n, UNKNOWN_INTERFACE_TYPE,
          functionName, interfaceType.toString()));
      return;
    }
  } catch (ClassNotFoundException e) {
    // Error handling for unknown types
    compiler.report(t.makeError(n, UNKNOWN_INTERFACE_TYPE,
        functionName, interfaceType.toString()));
    return;
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
    }
    currentProperties.put(name, interfaceType);
  }

  // Recursive function call protection
  Set<ObjectType> visitedInterfaces = new HashSet<>();
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    if (!visitedInterfaces.contains(iType)) {
      visitedInterfaces.add(iType);
      checkInterfaceConflictProperties(t, n, functionName, properties,
          currentProperties, iType);
    }
  }
}