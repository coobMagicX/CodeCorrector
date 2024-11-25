String getReadableJSTypeName(Node n, boolean dereference) {

  JSType type = getJSType(n);

  if (dereference) {
    ObjectType dereferenced = type.dereference();
    if (dereferenced != null) {
      type = dereferenced;
    }
  }

  String qualifiedName = n.getQualifiedName();
  if (qualifiedName != null) {
    return qualifiedName;
  }

  // If we're analyzing a GETPROP, the property may be inherited by the prototype chain.
  if (n.isGetProp()) {
    ObjectType objectType = type.toObjectType();

    if (objectType == null || !objectType.getConstructor().isInterface()) {
      String propName = n.getLastChild().getString();
      while (objectType != null && !objectType.hasOwnProperty(propName)) {
        objectType = objectType.getImplicitPrototype();
      }
    }

    // Don't show complex function names or anonymous types.
    if (objectType != null &&
        (objectType.getConstructor() != null ||
         objectType.isFunctionPrototypeType())) {
      return objectType.toString() + "." + propName;
    }
  }

  // If it's not a GETPROP, or the property wasn't found in the prototype chain,
  // we fall back to returning the type itself.
  if (type.isFunctionPrototypeType() ||
      (type.toObjectType() != null &&
       type.toObjectType().getConstructor() != null)) {
    return type.toString();
  } else if (type.isFunctionType()) {
    return "function";
  } else {
    return type.toString();
  }
}