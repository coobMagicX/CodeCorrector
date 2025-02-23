String getReadableJSTypeName(Node n, boolean dereference) {
    JSType type = getJSType(n);
    if (n.isGetProp() && type instanceof ObjectType) {
        ObjectType objectType = (ObjectType) (dereference ? type.dereference() : type);
        String propName = n.getLastChild().getString();

        ObjectType definingType = objectType;
        while (definingType != null && !definingType.hasOwnProperty(propName)) {
            definingType = definingType.getImplicitPrototype();
        }

        // If defining type is found where property is defined, retrieve it.
        if (definingType != null && definingType.hasOwnProperty(propName)) {
            objectType = definingType;
        }

        // Special handling for interfaces or classes that define the property
        if (objectType.getConstructor() != null) {
            JSType constructorType = objectType.getConstructor();
            if (constructorType instanceof FunctionType) {
                FunctionType functionType = (FunctionType) constructorType;
                if (functionType.isInterface()) {
                    ObjectType interfaceDefiningType = FunctionType.getTopDefiningInterface(objectType, propName);
                    if (interfaceDefiningType != null) {
                        objectType = interfaceDefiningType;
                    }
                }
            }
        }

        // Incorporate the property name for better context in output
        if (objectType.getDisplayName() != null) {
            return objectType.getDisplayName() + "." + propName;
        } else {
            // Fallback to the object type's toString method
            return objectType.toString() + "." + propName;
        }
    }

    // For other node types or if no property name exists, handle gracefully
    if (dereference) {
        ObjectType dereferenced = type.dereference();
        if (dereferenced != null) {
            type = dereferenced;
        }
    }

    // Return different representations based on the type characteristics
    String qualifiedName = n.getQualifiedName();
    if (qualifiedName != null) {
        return qualifiedName;
    } else if (type.isFunctionPrototypeType() ||
               (type.toMaybeObjectType() != null && type.toMaybeObjectType().getConstructor() != null)) {
        return type.toString();
    } else {
        String typeString = type.toString();
        return typeString.isEmpty() ? "Unknown type" : typeString;
    }
}
