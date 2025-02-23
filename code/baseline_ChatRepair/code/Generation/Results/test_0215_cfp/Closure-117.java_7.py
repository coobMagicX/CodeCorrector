String getReadableJSTypeName(Node n, boolean dereference) {
  if (n.isGetProp()) {
    ObjectType objectType = getJSType(n.getFirstChild()).dereference();
    if (objectType != null) {
      String propName = n.getLastChild().getString();
      ObjectType origObjectType = objectType;
      if (objectType.getConstructor() != null && objectType.getConstructor().isInterface()) {
        objectType = FunctionType.getTopDefiningInterface(objectType, propName);
      } else {
        while (objectType != null && !objectType.hasOwnProperty(propName)) {
          objectType = objectType.getImplicitPrototype();
        }
      }
      // if objectType becomes null, revert to original objectType for name extraction
      if (objectType == null) {
        objectType = origObjectType;
      }

      // Prefer using qualified name for clarity and avoiding unresolved names in deep prototypes
      if (objectType.hasDisplayName()) {
        return objectType.getDisplayName() + "." + propName;
      } else if (objectType.toString() != null) {
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
    // Don't show complex function names, use a simplified description.
    return "function";
  } else {
    return type.toString();
  }
}
