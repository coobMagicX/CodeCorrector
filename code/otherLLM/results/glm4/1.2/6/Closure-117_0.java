String getReadableJSTypeName(Node n, boolean dereference) {

  JSType type = getJSType(n);
  if (type == null) {
    return "unknown";
  }

  ObjectType objectType = type.toObjectType();
  if (objectType != null && dereference) {
    objectType = objectType.dereference();
    if (objectType == null) {
      objectType = type.toObjectType();
    }
  }

  // The best type name is the actual type name.
  String typeName = type.toString();

  if (n.isGetProp()) {
    String propName = n.getLastChild().getString();
    if (objectType != null && !expectNotNullOrUndefined(null, n, objectType, "Expected an object type", ObjectType.INSTANCE)) {
      // If the property is not found on the object type itself,
      // traverse up the prototype chain.
      while (objectType != null) {
        if (objectType.hasOwnProperty(propName)) {
          break;
        }
        objectType = objectType.getImplicitPrototype();
      }
    }

    if (objectType != null && (objectType.getConstructor() != null || objectType.isFunctionPrototypeType())) {
      typeName += "." + propName;
    }
  } else {
    // For non-GETPROP nodes, check if the type is a function prototype or
    // has a constructor that indicates it's an object.
    if (type.isFunctionPrototypeType() ||
        (objectType != null && objectType.getConstructor() != null)) {
      typeName = objectType.toString();
    }
  }

  String qualifiedName = n.getQualifiedName();
  if (qualifiedName != null) {
    return qualifiedName;
  } else if (type.isFunctionType()) {
    // Don't show complex function names.
    return "function";
  } else {
    return typeName;
  }
}