private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
                                              String functionName, HashMap<String, ObjectType> properties,
                                              HashMap<String, ObjectType> currentProperties,
                                              ObjectType interfaceType) {
    
    ObjectType implicitProto = interfaceType.getImplicitPrototype();

    if (implicitProto != null) {
        
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
    }

    
    List<ObjectType> extendedInterfaces = interfaceType.getCtorExtendedInterfaces();
    if (extendedInterfaces != null) {
        for (ObjectType iType : extendedInterfaces) {
            if (iType != null) {
                checkInterfaceConflictProperties(t, n, functionName, properties,
                                                 currentProperties, iType);
            }
        }
    }
}
