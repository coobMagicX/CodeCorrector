private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  
  // Check if the implicit prototype exists before accessing its property names
  if (implicitProto != null) {
    Set<String> currentPropertyNames = implicitProto.getOwnPropertyNames();
    for (String name : currentPropertyNames) {
      ObjectType oType = properties.get(name);
      if (oType != null) {
        // Check if the property type in the extended interface is equivalent to the one in the properties map
        if (!interfaceType.getPropertyType(name).isEquivalentTo(oType.getPropertyType(name))) {
          compiler.report(t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
              functionName, name, oType.toString(), interfaceType.toString()));
        }
      } else {
        // If the property does not exist in the properties map, add it to currentProperties
        currentProperties.put(name, interfaceType);
      }
    }
  }
  
  // Check and recurse on each of the interfaces extended by the current interface
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties, currentProperties, iType);
  }
}