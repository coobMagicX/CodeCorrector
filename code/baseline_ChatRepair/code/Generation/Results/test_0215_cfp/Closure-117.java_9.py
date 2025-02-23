String getReadableJSTypeName(Node n, boolean dereference) {

  // The best type name is the actual type name.

  if (n.isGetProp()) {
    ObjectType objectType = getJSType(n.getFirstChild()).dereference();
    if (objectType != null) {
      String propName = n.getLastChild().getString();
      String originalObjectName = objectType.getReferenceName();
      
      // Climb prototype chain if necessary.
      if (!objectType.hasOwnProperty(propName)) {
        while (objectType != null && !objectType.hasOwnProperty(propName)) {
          objectType = objectType.getImplicitPrototype();
          if (objectType != null) {
            originalObjectName = objectType.getReferenceName();
          }
        }
      }
      
      if (originalObjectName != null && objectType.hasOwnProperty(propName)) {
        return originalObjectName + "." + propName;
      }
    }
  }

  JSType type = getJSType(n);
  if (dereference) {
    ObjectType dereferenced = type.dereference();
    if (dereferenced != null) {
      type = dereferenced;
    }
  }
  if (type.isFunctionPrototypeType() ||
      (type.toObjectType() != null &&
       type.toObjectType().getConstructor() != null)) {
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
