private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj == null || !constraintObj.isRecordType()) {
    return;
  }

  ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
  if (objType == null) {
    return;
  }

  for (String prop : constraintObj.getOwnPropertyNames()) {
    JSType constraintPropType = constraintObj.getPropertyType(prop);
    if (objType.hasProperty(prop)) {
      JSType existingPropType = objType.getPropertyType(prop);
      if (existingPropType == null || existingPropType.isUnknownType()) {
        objType.defineInferredProperty(prop, constraintPropType, null);
      }
    } else {
      // Only infer new properties if they are not already defined
      objType.defineInferredProperty(prop, constraintPropType, null);
    }
  }
}
