String getReadableJSTypeName(Node n, boolean dereference) {

  JSType type = getJSType(n);
  if (dereference) {
    ObjectType dereferenced = type.dereference();
    if (dereferenced != null) {
      type = dereferenced;
    }
  }

  // The best type name is the actual type name.
  String typeName = type.toString();

  // If we're analyzing a GETPROP, the property may be inherited by the
  // prototype chain. So climb the prototype chain and find out where
  // the property was originally defined.
  if (n.isGetProp()) {
    ObjectType objectType = type.toObjectType();
    if (objectType != null && objectType.getConstructor() != null) {
      if (objectType.getConstructor().isInterface()) {
        objectType = FunctionType.getTopDefiningInterface(objectType, n.getLastChild().getString());
      } else {
        // classes
        while (objectType != null && !objectType.hasOwnProperty(n.getLastChild().getString())) {
          objectType = objectType.getImplicitPrototype();
        }
      }

      if (objectType != null) {
        typeName = objectType.toString() + "." + n.getLastChild().getString();
      }
    }
  }

  if (type.isFunctionPrototypeType() || type.toObjectType() != null && type.toObjectType().getConstructor() != null) {
    return type.toString();
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