import java.util.HashMap;
import java.util.Set;

class NodeTraversal {
    // Simulated methods and fields
    public Error makeError(Node node, String errorType, String... details) {
        return new Error(errorType + ": " + String.join(", ", details));
    }
}

class Node {
    // Node class implementation
}

class ObjectType {
    public ObjectType getImplicitPrototype() {
        return this; // Stub method
    }

    public Set<String> getOwnPropertyNames() {
        return Set.of("property1", "property2"); // Stub method
    }

    public boolean isEquivalentTo(ObjectType other) {
        return true; // Stub method
    }

    public ObjectType getPropertyType(String name) {
        return this; // Stub method
    }

    public Set<ObjectType> getCtorExtendedInterfaces() {
        return Set.of(this); // Stub method
    }
    
    @Override
    public String toString() {
        return "ObjectType"; // Stub method
    }
}

class Compiler {
    public void report(Error error) {
        System.out.println(error.message); // Simulated error reporting
    }
}

class Error {
    String message;

    public Error(String message) {
        this.message = message;
    }
}

public class InterfaceChecker {
    private static final String INCOMPATIBLE_EXTENDED_PROPERTY_TYPE = "Incompatible extended property type";
    private Compiler compiler;

    public InterfaceChecker(Compiler compiler) {
        this.compiler = compiler;
    }

    private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
                                                  String functionName, HashMap<String, ObjectType> properties,
                                                  HashMap<String, ObjectType> currentProperties,
                                                  ObjectType interfaceType) {
        ObjectType implicitProto = interfaceType.getImplicitPrototype();
        Set<String> currentPropertyNames;
        // This can be the case if interfaceType is a proxy to a non-existent
        // object (which is a bad type annotation, but shouldn't crash).
        if (implicitProto != null) {
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
                currentProperties.put(name, interfaceType.getPropertyType(name));
            }
        }

        for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
            checkInterfaceConflictProperties(t, n, functionName, properties,
                    currentProperties, iType);
        }
    }
}