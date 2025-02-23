private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) { // If property type is not declared, consider inferring it
          JSType existingType = objType.getPropertyType(prop); // Get existing property type
          // Define type to infer. If existing type is not null, infer it.
          JSType typeToInfer = existingType != null ? existingType : propType;
          if (!objType.hasProperty(prop) && existingType == null) {
            // If property doesn't exist and no existing type, use NONEXISTENT property type
            typeToInfer = propType.getLeastSupertype(getNativeType(JSTypeNative.UNKNOWN_TYPE));
          }
          // Now, define or redefine the inferred property
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}
