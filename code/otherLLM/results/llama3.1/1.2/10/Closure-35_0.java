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
          // Removed the unnecessary call when property exists in 'type' object
          objType.defineInferredProperty(prop, typeToInfer, null);
        } else {
          // Check if the actual type of the property matches the expected type in the constraint
          JSType actualPropType = objType.getPropertyType(prop);
          if (!actualPropType.equals(propType)) {
            // If types do not match, infer a new type for the property
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(actualPropType);
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}