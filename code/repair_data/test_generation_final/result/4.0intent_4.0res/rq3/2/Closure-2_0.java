import java.util.HashMap;
import java.util.Set;

private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
                                              String functionName, HashMap<String, ObjectType> properties,
                                              HashMap<String, ObjectType> currentProperties,
                                              ObjectType interfaceType) {
    ObjectType implicitProto = interfaceType.getImplicitPrototype();

    if (implicitProto == null) {
        return; // Return or handle the case where implicitProto is null
    }

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
        currentProperties.put(name, interfaceType.getPropertyType(name)); // Ensure to store the specific property type
    }

    for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
        checkInterfaceConflictProperties(t, n, functionName, properties,
            currentProperties, iType);
    }
}