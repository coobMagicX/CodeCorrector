String getReadableJSTypeName(Node n, boolean dereference) {
  String propName = null;
  ObjectType objectType = null;

  // Handle property names for property access expressions.
  if (n.isGetProp()) {
    propName = n.getLastChild().getString();
    objectType = getJSType(n.getFirstChild()).dereference();
  
    if (objectType != null) {
      // Attempt to find the most specific type where the property is defined
      ObjectType mostSpecificType = objectType;
      while (!mostSpecificType.hasOwnProperty(propName) && mostSpecificType.getImplicitPrototype() != null) {
        mostSpecificType = mostSpecificType.getImplicitPrototype();
      }
      
      if (mostSpecificType.hasOwnProperty(propName)) {
        objectType = mostSpecificType;
      }
      
      // Check for interfaces
      if (objectType.getConstructor() != null && objectType.getConstructor().isInterface()) {
        ObjectType interfaceType = FunctionType.getTopDefiningInterface(objectType, propName);
        if (interfaceType != null) {
          objectType = interfaceType;
        }
      }
    }

    // Format the output typeName.propName
    if (objectType != null && objectType.getDisplayName() != null) {
      return objectType.getDisplayName() + "." + propName;
    }
  }

  // General type retrieval and dereferencing if needed
  JSType type = getJSType(n);
  if (dereference) {
    ObjectType dereferenced = type.dereference();
    if (dereferenced != null) {
      type = dereferenced;
    }
  }
  
  // Return readable names depending on the type's characteristics
  if (type.isFunctionPrototypeType() || 
      (type.toObjectType() != null && type.toObjectType().getConstructor() != null)) {
    return type.toObjectType().toString();
  } else {
    // Ensure use of qualified name if available, fallback to type's string representation otherwise.
    String qualifiedName = n.getQualifiedName();
    return qualifiedName != null ? qualifiedName : type.toString();
  }
}
