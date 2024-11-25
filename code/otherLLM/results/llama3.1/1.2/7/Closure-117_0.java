String getReadableJSTypeName(Node n, boolean dereference) {

  // The best type name is the actual type name.

  // If we're analyzing a GETPROP, the property may be inherited by the
  // prototype chain. So climb the prototype chain and find out where
  // the property was originally defined.
  if (n.isGetProp()) {
    ObjectType objectType = getJSType(n.getFirstChild()).dereference();
    if (objectType != null) {
      String propName = n.getLastChild().getString();
      if (objectType.getConstructor() != null &&
          objectType.getConstructor().isInterface()) {
        objectType = FunctionType.getTopDefiningInterface(
            objectType, propName);
      } else {
        // classes
        while (objectType != null && !objectType.hasOwnProperty(propName)) {
          ObjectType prototype = objectType.getImplicitPrototype();
          if (prototype == null) break;
          objectType = prototype;
        }
      }

      // Don't show complex function names or anonymous types.
      // Instead, try to get a human-readable type name.
      if (objectType != null &&
          (objectType.getConstructor() != null ||
           objectType.isFunctionPrototypeType())) {
        return objectType.toString() + "." + propName;
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
    // Don't show complex function names.
    return "function";
  } else {
    if (type.toObjectType() != null && type.toObjectType().getConstructor() == null) {
      return "object"; // Changed from 'Object' to 'object'
    }
    return type.toString();
  }
}