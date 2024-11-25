String getReadableJSTypeName(Node n, boolean dereference) {

  // The best type name is the actual type name.

  // If we're analyzing a GETPROP, the property may be inherited by the
  // prototype chain. So climb the prototype chain and find out where
  // the property was originally defined.
  if (n.isGetProp()) {
    JSType baseType = getJSType(n.getFirstChild());
    ObjectType objectType = baseType.dereference();
    String propName = n.getLastChild().getString();

    while (objectType != null && !objectType.hasOwnProperty(propName)) {
      objectType = objectType.getImplicitPrototype();
    }

    if (objectType != null) {
      if (objectType.getConstructor() != null && objectType.getConstructor().isInterface()) {
        objectType = FunctionType.getTopDefiningInterface(objectType, propName);
      }
      
      // Don't show complex function names or anonymous types.
      // Instead, try to get a human-readable type name.
      if ((objectType.getConstructor() != null || objectType.isFunctionPrototypeType())) {
        return objectType.toString() + "." + propName;
      }
    }
  }

  JSType type = getJSType(n);
  ObjectType dereferencedType = type.dereference();
  if (dereferencedType != null) {
    type = dereferencedType;
  }

  if (type.isFunctionPrototypeType() || (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
    return type.toString();
  }
  
  String qualifiedName = n.getQualifiedName();
  if (qualifiedName != null) {
    return qualifiedName;
  } else if (type.isFunctionType()) {
    // Don't show complex function names.
    return "function";
  } else {
    return type.toString();
  }
}