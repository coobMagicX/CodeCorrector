String getReadableJSTypeName(Node n, boolean dereference) {
    if (n.isGetProp()) {
        String propName = n.getLastChild().getString(); // Get the property name
        JSType jsType = getJSType(n.getFirstChild()); // Get the type of the node to the left of the property

        if (dereference && jsType != null) {
            jsType = jsType.dereference(); // Dereference the type if necessary
        }
        
        if (jsType instanceof ObjectType) {
            ObjectType objectType = (ObjectType) jsType;
            ObjectType definingType = null;

            // Traverse the prototype chain to find the property definition if not found on current object
            while (objectType != null) {
                if (objectType.hasOwnProperty(propName)) {
                    definingType = objectType;
                    break;
                }
                objectType = objectType.getImplicitPrototype();
            }

            // If an interface originally defines the property, change the objectType to that interface
            if (definingType != null && definingType.getConstructor() != null &&
                definingType.getConstructor().isInterface()) {
                definingType = FunctionType.getTopDefiningInterface(definingType, propName);
            }

            // Construct the readable name if a proper definition was found
            if (definingType != null) {
                return definingType.toString() + "." + propName;
            }
        }
    }

    JSType type = getJSType(n);
    if (dereference && type != null) {
        type = type.dereference();
    }

    // For non-property accesses or fallback if no defining type found
    if (type.isFunctionPrototypeType() || 
       (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
        return type.toString();
    }
    
    String qualifiedName = n.getQualifiedName();
    if (qualifiedName != null) {
        return qualifiedName;
    } else if (type.isFunctionType()) {
        return "function";
    } else {
        return type.toString();
    }
}
