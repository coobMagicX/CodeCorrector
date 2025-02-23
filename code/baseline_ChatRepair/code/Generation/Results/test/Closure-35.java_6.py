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
          // Check if the property is actually present in objType before inferring a type.
          if (!objType.hasProperty(prop)) {
            // Previously, VOID_TYPE was used, leading to potential incorrect property typification,
            // especially if propType itself isn't VOID_TYPE. We should use a common supertype strategy.
            // Assuming here that getting the common supertype might resolve the unintended warning.
            typeToInfer = propType.getLeastSupertype(getNativeType(JSTypeNative.UNKNOWN_TYPE));
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}
