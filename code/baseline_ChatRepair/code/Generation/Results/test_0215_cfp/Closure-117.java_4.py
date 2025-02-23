String getReadableJSTypeName(Node n, boolean dereference) {
    if (n.isGetProp()) {
        String propName = n.getLastChild().getString();
        JSType jsType = getJSType(n.getFirstChild());

        if (dereference) {
            jsType = jsType.dereference();
        }

        if (jsType instanceof ObjectType) {
            ObjectType objectType = (ObjectType) jsType;
            // Traverse the prototype chain to find the definition.
            while (objectType != null && !objectType.hasOwnProperty(propName)) {
                objectType = objectType.getImplicitPrototype();
            }

            if (objectType != null) {
                ObjectType constructor = objectType.getConstructor();
                if (constructor != null && constructor.isInterface()) {
                    objectType = FunctionType.getTopDefiningInterface(objectType, propName);
                }

                // Include the property name only if it is defined on some prototype.
                if (objectType != null) {
                    return objectType.toString() + "." + propName;
                }
            }
        }
    }

    JSType type = getJSType(n);
    if (dereference) {
        type = type.dereference();
    }
    // Return generic or simple names if specific names are not demanded.
    if (type.isFunctionPrototypeType() || (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
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
