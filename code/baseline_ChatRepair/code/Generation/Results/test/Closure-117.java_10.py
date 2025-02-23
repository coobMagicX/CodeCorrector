String getReadableJSTypeName(Node n, boolean dereference) {

  // Default approach to resolve the most suitable type name
  if (n.isGetProp()) {
    ObjectType objectType = getJSType(n.getFirstChild()).dereference();
    if (objectType != null) {
      String propName = n.getLastChild().getString();
      
      // Climb prototype chain to find original definition with actual property name defined
      ObjectType definingType = objectType;
      while (definingType != null && !definingType.hasOwnProperty(propName)) {
        definingType = definingType.getPrototypeObject();
      }
      
      // Verify if the definingType is well-established to avoid possible errors in missing type cases
      if (definingType != null) {
        String typeName = definingType.toAnnotationString();
        return typeName + "." + propName;
      }
    }
  }

  // Other type processing for nodes that do not specifically involve property definitions
  JSType type = getJSType(n);
  if (dereference) {
    type = type.dereference(); // Adjust type considering dereferencing
  }
  
  // Return formatted type descriptors based on the object properties, functions and framework structures
  if (type.isFunctionPrototypeType() || (type.toMaybeObjectType() != null && type.toMaybeObjectType().getConstructor() != null)) {
    return type.toMaybeObjectType().toString();
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
