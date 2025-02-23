String getReadableJSTypeName(Node n, boolean dereference) {
  JSType type = getJSType(n);
  if (dereference) {
    ObjectType dereferenced = type.dereference();
    if (dereferenced != null) {
      type = dereferenced;
    }
  }

  if (n.isGetProp()) {
    ObjectType objectType = type.toMaybeObjectType();
    if (objectType != null) {
      String propName = n.getLastChild().getString();
      ObjectType definingType = objectType;
      
      if (objectType.getConstructor() != null && objectType.getConstructor().isInterface()) {
        definingType = FunctionType.getTopDefiningInterface(objectType, propName);
      } else {
        // Climb up the prototype chain to find where the property is defined.
        while (definingType != null && !definingType.hasOwnProperty(propName)) {
          definingType = definingType.getImplicitPrototype();
        }
      }
      
      // If property was found in a prototype, use it, else use the original type.
      if (definingType != null) {
        objectType = definingType;
      }
    
      // Ensure to return meaningful type information based on constructor or function prototype status.
      return objectType.toAnnotationString() + "." + propName;
    }
  }
  
  // Handle cases where node is not a property access.
  if (type.isFunctionPrototypeType() || (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
    return type.toAnnotationString();
  }
  String qualifiedName = n.getQualifiedName();
  if (qualifiedName != null) {
    return qualifiedName;
  } else if (type.isFunctionType()) {
    return "function";
  } else {
    return type.toAnnotationString();
  }
}
