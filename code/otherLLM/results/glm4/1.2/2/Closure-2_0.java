private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  
  if (interfaceType == null) {
    compiler.report(t.makeError(n, NON_EXISTENT_INTERFACE, functionName));
    return;
  }

  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  if (implicitProto == null) {
    compiler.report(t.makeError(n, NULL_IMPLICIT_PROTO, functionName));
    return;
  }
  
  Set<String> currentPropertyNames = implicitProto.getOwnPropertyNames();
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      if (!interfaceType.getPropertyType(name).isEquivalentTo(oType.getPropertyType(name))) {
        compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
            functionName, name, oType.toString(), interfaceType.toString()));
      }
    } else {
      properties.put(name, interfaceType);
    }
  }

  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }
}