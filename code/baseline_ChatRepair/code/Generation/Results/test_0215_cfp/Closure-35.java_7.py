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
        if (objType.hasProperty(prop) && !objType.isPropertyTypeDeclared(prop)) {
          // If the property is present but its type is not declared, infer the type.
          objType.defineInferredProperty(prop, propType, null);
        } else if (!objType.hasProperty(prop)) {
          // If the property isn’t present, define it as an inferred property.
          // Previous versions had issues by handling VOID_TYPE and supertypes incorrectly.
          objType.defineInferredProperty(prop, propType, null);
        }
      }
    }
  }
}
