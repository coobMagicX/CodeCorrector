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
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType existingType = objType.getPropertyType(prop);
          // Only infer type if no existing type or if existing type is looser than propType
          if (existingType == null || existingType.isUnknownType()) {
            objType.defineInferredProperty(prop, propType, null);
          } else if (existingType != propType) {
            // Define the type to the most specific type available to meet existing constraints
            JSType moreSpecificType = existingType.getGreatestSubtype(propType);
            objType.defineInferredProperty(prop, moreSpecificType, null);
          }
        }
      }
    }
  }
}
