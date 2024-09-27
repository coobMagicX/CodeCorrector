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
        JSType existingPropType = objType.getPropertyType(prop);
        
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          } else if (existingPropType != null) {
            // Adjusting type inference to ensure that if a property exists,
            // the type is refined to the most specific type possible which is
            // still a supertype of all observed types.
            typeToInfer = existingPropType.getGreatestSubtype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}