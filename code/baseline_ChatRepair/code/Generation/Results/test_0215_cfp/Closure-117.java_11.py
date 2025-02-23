String getReadableJSTypeName(Node n, boolean dereference) {
  // If the node is a property access (GETPROP), find where the property is defined.
  if (n.isGetProp()) {
    ObjectType objectType = getJSType(n.getFirstChild()).dereference();
    if (objectType != null) {
      String propName = n.getLastChild().getString();

      // Climb the prototype chain until the property is found or the chain ends.
      while (objectType != null && !objectType.hasOwnProperty(propName)) {
        objectType = objectType.getImplicitPrototype();
      }

      // Ensure that now objectType either points to null (not found case)
      // or to the correct ObjectType where the property is defined.
      if (objectType != null) {
        return objectType.toString() + "." + propName;
      }
    }
  }

  // Get type of the node and optionally dereference it.
  JSType type = getJSType(n);
  if (dereference) {
    type = type.dereference();
  }

  // If the type is a function prototype or a constructor exists, return the type's string representation.
  if (type.isFunctionPrototypeType() ||
      (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
    return type.toString();
  }

  // Return the qualified name of the node if it's available.
  String qualifiedName = n.getQualifiedName();
  if (qualifiedName != null) {
    return qualifiedName;
  }

  // Handle function types as just 'function' to avoid complexity.
  else if (type.isFunctionType()) {
    return "function";
  }

  // Fallback: use the type's own string representation.
  return type.toString();
}
