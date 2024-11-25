private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj =
      ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          // Ensure that the property exists in the object type before inferring
          if (!objType.hasProperty(prop)) {
            // Use getLeastSupertype with VOID_TYPE to ensure compatibility
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          // Define the inferred property only if it's different from the current type
          if (typeToInfer != null && !objType.getPropertyType(prop).equals(typeToInfer)) {
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}