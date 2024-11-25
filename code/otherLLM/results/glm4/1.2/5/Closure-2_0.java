private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  // Ensure proper error handling when `interfaceType.getImplicitPrototype()` returns null.
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  if (implicitProto == null) {
    compiler.report(t.makeError(n, NULL_IMPLICIT_PROTOTYPE, functionName));
    return; // Stop processing and exit the method
  }

  Set<String> currentPropertyNames;
  try {
    currentPropertyNames = implicitProto.getOwnPropertyNames();
  } catch (Exception e) {
    compiler.report(t.makeError(n, ERROR_GETTING_PROPERTIES, functionName));
    return; // Stop processing and exit the method
  }

  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      // Check if the extended interfaces exist before attempting to report errors or traverse them.
      ObjectType extInterface = interfaceType.getExtendedInterface(name);
      if (extInterface == null) {
        compiler.report(t.makeError(n, NON_EXISTENT_EXTENDED_INTERFACE,
            functionName, name));
        continue; // Skip this property and move on to the next
      }

      // Confirm that the `properties` HashMap contains all the necessary types associated with the properties being checked.
      ObjectType currentType = currentProperties.get(name);
      if (currentType == null) {
        compiler.report(t.makeError(n, MISSING_CURRENT_PROPERTY_TYPE,
            functionName, name));
        continue; // Skip this property and move on to the next
      }

      if (!extInterface.getPropertyType(name).isEquivalentTo(oType)) {
        compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
            functionName, name, oType.toString(), extInterface.toString()));
      }
    }
    currentProperties.put(name, interfaceType);
  }

  // Only traverse extended interfaces if they exist.
  Set<String> extendedInterfaces = interfaceType.getExtendedInterfaces();
  for (String iName : extendedInterfaces) {
    ObjectType iType = interfaceType.getInterface(iName);
    if (iType != null) { // This check ensures the interface exists
      checkInterfaceConflictProperties(t, n, functionName, properties,
          currentProperties, iType);
    }
  }
}